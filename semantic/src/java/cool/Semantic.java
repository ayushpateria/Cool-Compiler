package cool;

import java.util.*;
import cool.AST.class_;

import java.util.Vector;
import java.util.Enumeration;

public class Semantic {
	private boolean errorFlag = false;

	public void reportError(String filename, int lineNo, String error) {
		errorFlag = true;
		System.err.println(filename + ":" + lineNo + ": " + error);
	}

	public void reportError(String error) {
		errorFlag = true;
		System.err.println(error);
	}

	public boolean getErrorFlag() {
		return errorFlag;
	}

	/*
		Don't change code above this line
	*/
	ScopeTable<AST.attr> scopeTable = new ScopeTable<AST.attr>();
	ClassTable classTable = new ClassTable();

	public Semantic(AST.program program) {
		/*
			Step 1:
					- Check for basic object/class redefinitions (which are invalid).
					- Check for invalid basic object/class inheritance.
					- Check for object/class cyclic inheritance (which are invalid).
		*/

		for (class_ c1 : program.classes) {
			String c1Name = c1.name;

			// Check for basic object/class redefinitions (which are invalid).
			if (c1Name.equals("Object")) {
				reportError(c1.filename, c1.lineNo, "Redefinition of basic class Object.");
			} else if (c1Name.equals("IO")) {
				reportError(c1.filename, c1.lineNo, "Redefinition of basic class IO.");
			} else if (c1Name.equals("Int")) {
				reportError(c1.filename, c1.lineNo, "Redefinition of basic class Int.");
			} else if (c1Name.equals("String")) {
				reportError(c1.filename, c1.lineNo, "Redefinition of basic class String.");
			} else if (c1Name.equals("Bool")) {
				reportError(c1.filename, c1.lineNo, "Redefinition of basic class Bool.");
			} else {
				String c2Name = c1.parent;
				if (c2Name.equals("SELF_TYPE")) {
					reportError(c1.filename, c1.lineNo, "Parent Class name cannot be SELF_TYPE");
					continue;
				}
				// Check for invalid basic object/class inheritance.
				if (c2Name.equals("Int")) {
					reportError(c1.filename, c1.lineNo, "Class " + c1Name + " cannot inherit class Int.");
				} else if (c2Name.equals("String")) {
					reportError(c1.filename, c1.lineNo, "Class " + c1Name + " cannot inherit class String.");
				} else if (c2Name.equals("Bool")) {
					reportError(c1.filename, c1.lineNo, "Class " + c1Name + " cannot inherit class Bool.");
				} else {
					classTable.classNameMapper.put(c1Name, c1);
					classTable.inheritanceGraph.addEdge(c2Name, c1Name);
				}
			}
		}
		// Check for object/class cyclic inheritance (which are invalid).
		for (Map.Entry pair : classTable.inheritanceGraph.getCycles().entrySet()) {
			class_ c = classTable.classNameMapper.get(pair.getValue());
			reportError(c.filename, c.lineNo, "Class " + pair.getValue() + ", or an ancestor of " + pair.getValue()
					+ ", is involved in an inheritance cycle.");
			c = classTable.classNameMapper.get(pair.getKey());
			reportError(c.filename, c.lineNo, "Class " + pair.getKey() + ", or an ancestor of " + pair.getKey()
					+ ", is involved in an inheritance cycle.");
		}

		// Check for inheriting a class that is not defined.
		for (class_ classy : program.classes) {
			String parentName = classy.parent;
			if (classTable.classNameMapper.get(parentName) == null) {
				reportError(classy.filename, classy.lineNo,
						"Error, inheriting from a Parent " + parentName + " that is not defined");
			}
		}

		// Step 1 Complete.

		/*
			Step 2: Create global object and method environments.
					- Check for multiply defined errors during this phase as well.
					- Check for existence of Main class and main method in main class.
					- Check for inherited attribute names being declared in derived class.
		*/

		Set<String> classNames = new HashSet<String>();
		boolean mainClassExists = false; //flag if Main class is defined
		boolean mainInMain = false; //flag if main() method exists in Main class
		boolean noFormalsInMainMethod = false; //flag if main() method has formals
		for (class_ c1 : program.classes) {
			if (c1.name.equals("Main"))
				mainClassExists = true;

			if (classNames.contains(c1.name)) {
				reportError(c1.filename, c1.lineNo, "Class " + c1.name + " was previously defined.");
				continue;
			} else {
				classNames.add(c1.name);
			}

			Map<String, List<String>> methodArgMap = new HashMap<String, List<String>>();
			Map<String, String> attrTypeMap = new HashMap<String, String>();
			for (AST.feature f : c1.features) {
				if (f instanceof AST.attr) {
					AST.attr a = (AST.attr) f;
					if (attrTypeMap.containsKey(a.name)) {
						reportError(c1.filename, c1.lineNo,
								"Attribute " + a.name.toString() + " is multiply defined in class.");
					} else if (isAttrInherited(a, c1, classTable)) {
						reportError(c1.filename, c1.lineNo,
								"Attribute " + a.name.toString() + " is an attribute of an inherited class.");
					} else {
						attrTypeMap.put(a.name, a.typeid);
					}
				} else if (f instanceof AST.method) {
					AST.method m = (AST.method) f;
					if (methodArgMap.containsKey(m.name)) {
						reportError(c1.filename, c1.lineNo, "Method " + m.name + " is multiply defined.");
						continue;
					} else {
						List<String> typeList = new ArrayList<String>();
						Set<String> formalNames = new HashSet<String>();
						for (AST.formal fo : m.formals) {
							if (formalNames.contains(fo.name)) {
								reportError(c1.filename, c1.lineNo, "Formal parameter " + fo.name + " in function "
										+ m.name + " in class " + c1.name + " is multiply defined");
								continue;
							} else {
								formalNames.add(fo.name);
							}

							if (fo.typeid.equals("SELF_TYPE")) {
								reportError(c1.filename, c1.lineNo, "Formal parameter " + fo.name + " in function "
										+ m.name + " in class " + c1.name + " cannot have type SELF_TYPE");
								continue;
							}
							typeList.add(fo.typeid);
						}
						typeList.add(m.typeid);
						methodArgMap.put(m.name, typeList);
						if (c1.name.equals("Main")) {
							if (m.name.equals("main")) {
								mainInMain = true;
								noFormalsInMainMethod = typeList.size() == 1;
							}
						}
					}
				} else {
					System.out.println("Error should never reach here!");
				}
			}
			classTable.methodEnv.insert(c1.name, methodArgMap);
			classTable.objectEnv.insert(c1.name, attrTypeMap);
		}

		// Making sure Main class exists and has a main() method.
		if (!mainClassExists) {
			reportError("Class Main is not defined.");
		} else {
			if (!mainInMain) {
				class_ main = classTable.classNameMapper.get("Main");
				reportError(main.filename, main.lineNo, "No 'main' method in class Main.");
			} else {
				if (!noFormalsInMainMethod) {
					class_ main = classTable.classNameMapper.get("Main");
					reportError(main.filename, main.lineNo, "'main' method in class Main should have no arguments.");
				}
			}
		}

		//Step 2 completed.


		//Step 3: Type Checking.

		for (class_ classie : program.classes) {
			for (AST.feature f : classie.features) {
				if (f instanceof AST.attr)
					SemantNode((AST.attr) f, classTable, classie);
				else if (f instanceof AST.method)
					SemantNode((AST.method) f, classTable, classie);
			}
		}
	}

	// Checking if an attribute exists in one of its parent classes.
	private boolean isAttrInherited(AST.attr a, class_ curr, ClassTable c) {
		Map<String, String> nodeParentMap = c.inheritanceGraph.getNodeParentHashMap("Object");
		String parentName = nodeParentMap.get(curr.name);
		while (parentName != null) {
			class_ parent = c.classNameMapper.get(parentName);
			for (AST.feature f : parent.features) {
				if (f instanceof AST.attr) {
					if (((AST.attr) f).name.equals(a.name))
						return true;
				}
			}
			parentName = nodeParentMap.get(parentName);
		}
		return false;
	}

	public void SemantNode(AST.method node, ClassTable c, class_ curr) {
		Map<String, List<String>> methodArgMap = (Map<String, List<String>>) c.methodEnv.lookUpGlobal(curr.name);
		if (methodArgMap == null) {
			reportError(curr.filename, curr.lineNo,
					"Unexpected Error occurred in method, current class not in methodEnv");
		} else {
			List<String> formalList = methodArgMap.get(node.name);
			if (formalList == null) {
				reportError(curr.filename, curr.lineNo,
						"Unexpected Error occurred in method, method name not in methodEnv");
			} else {
				List<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();
				newBindings.add(new Pair<String, String>("self", curr.name));
				for (AST.formal formy : node.formals) {
					String namey = formy.name;
					String typey = formy.typeid;
					newBindings.add(new Pair<String, String>(namey, typey));
				}
				Helper.updateObjectEnv(c.objectEnv, curr, newBindings); //Enter a new scope with new bindings
				SemantNode(node.body, c, curr);
				c.objectEnv.exitScope(); //Restore old scope

				String T0_prime_string = node.body.type;

				if (c.classNameMapper.get(node.typeid) == null) {
					reportError(curr.filename, node.lineNo,
							"Undefined return type " + node.typeid + " in method " + node.name + ".");
				} else if (!c.inheritanceGraph.conforms(T0_prime_string, node.typeid, "Object")) {
					reportError(curr.filename, node.lineNo, "Inferred return type " + T0_prime_string + " of method "
							+ node.name + " does not conform to declared return type " + node.typeid + ".");
				}
			}
		}

	}

	public void SemantNode(AST.attr node, ClassTable c, class_ curr) {
		Map<String, String> attrTypeMap = (Map<String, String>) c.objectEnv.lookUpGlobal(curr.name);
		if (attrTypeMap == null) {

			reportError(curr.filename, node.lineNo,
					"Unexpected Error occurred in attr, current class not in objectEnv");
		} else {
			String T0 = attrTypeMap.get(node.name);
			if (T0 == null) {

				reportError(curr.filename, node.lineNo, "Unexpected Error occurred in attr: attr name " + node.name
						+ " is not in objectEnv for class " + curr.name
						+ " this is usually caused if you have multiply defined or redifining inherited variable errors so fix those please.");
			} else {
				if (!T0.equals(node.typeid)) {

					reportError(curr.filename, node.lineNo, "Unexpected Error occurred in attr: type " + T0.toString()
							+ " in objectEnv not equal to type_decl " + node.typeid
							+ ". This is usually caused if you have mutiply defined or redifining inherited variable errors so fix those please.");
				} else {
					if (!(node.value instanceof AST.no_expr)) {
						List<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();

						Helper.updateObjectEnv(c.objectEnv, curr, newBindings); //Enter a new scope with new bindings.
						SemantNode(node.value, c, curr);
						String T1 = node.value.type;
						if (!c.inheritanceGraph.conforms(T1.toString(), T0.toString(), "Object")) {

							reportError(curr.filename, node.lineNo,
									"Inferred type " + T1.toString() + " of initialization of attribute " + node.name
											+ " does not conform to declared type " + node.typeid + ".");
						}
						c.objectEnv.exitScope(); //Restore old scope.
					}
				}
			}
		}

	}

	public void SemantNode(AST.block node, ClassTable c, class_ curr) {
		for (AST.expression expr : node.l1) {
			SemantNode(expr, c, curr);
			node.type = expr.type;
		}
	}

	public void SemantNode(AST.assign node, ClassTable c, class_ curr) {
		Map<String, String> varMap = (Map<String, String>) c.objectEnv.lookUpGlobal(curr.name);
		if (varMap == null) {
			reportError(curr.filename, node.lineNo,
					"Unexpected Error occurred in assign, current class not in objectEnv");
			node.type = "Object";
		} else {
			String type = Helper.attrType(node.name, curr, c);
			if (type == null) {

				reportError(curr.filename, node.lineNo,
						"Identifier: " + node.name + " in class " + curr.name.toString() + " is undefined");
				node.type = "Object";
			} else {
				SemantNode(node.e1, c, curr);
				String type_prime = node.e1.type;
				if (c.inheritanceGraph.conforms(type_prime.toString(), type.toString(), "Object")) {
					node.type = type_prime;
				} else {

					reportError(curr.filename, node.lineNo,
							"Type " + type_prime.toString()
									+ " of assigned expression does not conform to declared type " + type.toString()
									+ " of identifier " + node.name + ".");
					node.type = "Object";
				}
			}
		}

	}

	public void SemantNode(AST.static_dispatch node, ClassTable c, class_ curr) {
		SemantNode(node.caller, c, curr);
		String T0 = node.caller.type;
		List<String> actualTypes = new ArrayList<String>();
		for (AST.expression exp : node.actuals) {
			SemantNode(exp, c, curr);
			actualTypes.add(exp.type);
		}

		if (!c.inheritanceGraph.conforms(T0.toString(), node.typeid.toString(), "Object")) {

			reportError(curr.filename, node.lineNo, "Expression type " + T0.toString()
					+ " does not conform to declared static dispatch type " + node.typeid.toString());
		}

		Map<String, List<String>> curr_methods_map = (Map<String, List<String>>) c.methodEnv.lookUpGlobal(node.typeid);
		if (curr_methods_map == null) {

			reportError(curr.filename, node.lineNo, "Unexpected error occurred in static dispatch. Class "
					+ node.typeid.toString() + " not in methodEnv");
			node.type = "Object";
		} else {
			List<String> formalTypes = curr_methods_map.get(node.name);
			if (formalTypes == null) {

				reportError(curr.filename, node.lineNo, "Dispatch to undefined method " + node.name + ".");
				node.type = "Object";
			} else {
				if (actualTypes.size() != formalTypes.size() - 1) {

					reportError(curr.filename, node.lineNo,
							"Method " + node.name + " called with wrong number of arguments.");
					node.type = "Object";
				} else {
					for (int i = 0; i < actualTypes.size(); i++) {
						if (!c.inheritanceGraph.conforms(actualTypes.get(i).toString(), formalTypes.get(i).toString(),
								"Object")) {

							reportError(curr.filename, node.lineNo, "Inferred type " + actualTypes.get(i).toString()
									+ " does not conform to formal type " + formalTypes.get(i).toString());
							node.type = "Object";
							return;
						}
					}
					String T_return;
					T_return = formalTypes.get(formalTypes.size() - 1);
					node.type = T_return;
				}
			}
		}
	}

	public void SemantNode(AST.expression node, ClassTable c, class_ curr) {

		if (node instanceof AST.assign)
			SemantNode((AST.assign) node, c, curr);
		else if (node instanceof AST.static_dispatch)
			SemantNode((AST.static_dispatch) node, c, curr);

		else if (node instanceof AST.dispatch)
			SemantNode((AST.dispatch) node, c, curr);

		else if (node instanceof AST.cond)
			SemantNode((AST.cond) node, c, curr);

		else if (node instanceof AST.loop)
			SemantNode((AST.loop) node, c, curr);

		else if (node instanceof AST.block)
			SemantNode((AST.block) node, c, curr);

		else if (node instanceof AST.let)
			SemantNode((AST.let) node, c, curr);

		else if (node instanceof AST.typcase)
			SemantNode((AST.typcase) node, c, curr);

		else if (node instanceof AST.new_)
			SemantNode((AST.new_) node, c, curr);

		else if (node instanceof AST.isvoid)
			SemantNode((AST.isvoid) node, c, curr);

		else if (node instanceof AST.plus)
			SemantNode((AST.plus) node, c, curr);

		else if (node instanceof AST.sub)
			SemantNode((AST.sub) node, c, curr);

		else if (node instanceof AST.mul)
			SemantNode((AST.mul) node, c, curr);

		else if (node instanceof AST.divide)
			SemantNode((AST.divide) node, c, curr);

		else if (node instanceof AST.comp)
			SemantNode((AST.comp) node, c, curr);

		else if (node instanceof AST.lt)
			SemantNode((AST.lt) node, c, curr);

		else if (node instanceof AST.leq)
			SemantNode((AST.leq) node, c, curr);

		else if (node instanceof AST.eq)
			SemantNode((AST.eq) node, c, curr);

		else if (node instanceof AST.neg)
			SemantNode((AST.neg) node, c, curr);

		else if (node instanceof AST.object)
			SemantNode((AST.object) node, c, curr);

		else if (node instanceof AST.int_const)
			SemantNode((AST.int_const) node, c, curr);

		else if (node instanceof AST.string_const)
			SemantNode((AST.string_const) node, c, curr);

		else if (node instanceof AST.bool_const)
			SemantNode((AST.bool_const) node, c, curr);

	}

	public void SemantNode(AST.dispatch node, ClassTable c, class_ curr) {
		SemantNode(node.caller, c, curr);
		String T0 = node.caller.type;
		List<String> actualTypes = new ArrayList<String>();
		for (AST.expression exp : node.actuals) {
			SemantNode(exp, c, curr);
			actualTypes.add(exp.type);
		}
		String T0_prime;
		T0_prime = T0;

		Map<String, List<String>> curr_methods_map = (Map<String, List<String>>) c.methodEnv.lookUpGlobal(curr.name);
		if (curr_methods_map == null) {

			reportError(curr.filename, node.lineNo,
					"Unexpected error occurred in dispatch. Class " + curr.name.toString() + " not in methodEnv");
			node.type = "Object";
		} else {
			List<String> formalTypes = Helper.getFormalList(node.name, c.classNameMapper.get(T0_prime.toString()), c);
			if (formalTypes == null) {

				reportError(curr.filename, node.lineNo, "Dispatch to undefined method " + node.name + ".");
				node.type = "Object";
			} else {
				if (actualTypes.size() != formalTypes.size() - 1) {

					reportError(curr.filename, node.lineNo,
							"Method " + node.name + " called with wrong number of arguments.");
					node.type = "Object";
				} else {
					for (int i = 0; i < actualTypes.size(); i++) {
						if (!c.inheritanceGraph.conforms(actualTypes.get(i).toString(), formalTypes.get(i).toString(),
								"Object")) {

							reportError(curr.filename, node.lineNo,
									"In call of method " + node.name + ", type " + actualTypes.get(i).toString()
											+ " of parameter " + "variable" + " does not conform to declared type "
											+ formalTypes.get(i).toString());
							node.type = "Object";
							return;
						}
					}
					String T_return;
					T_return = formalTypes.get(formalTypes.size() - 1);
					node.type = T_return;
				}
			}
		}
	}

	public void SemantNode(AST.cond node, ClassTable c, class_ curr) {
		SemantNode(node.predicate, c, curr);
		if (!node.predicate.type.equals("Bool")) {

			reportError(curr.filename, node.lineNo, "Predicate of conditional has to be boolean");
			node.type = "Object";
		}
		SemantNode(node.ifbody, c, curr);
		SemantNode(node.elsebody, c, curr);
		node.type = c.classNameMapper.get(c.inheritanceGraph.lub(node.ifbody.type, node.elsebody.type, "Object")).name;
	}

	public void SemantNode(AST.loop node, ClassTable c, class_ curr) {
		SemantNode(node.predicate, c, curr);
		String T1 = node.predicate.type;
		if (!T1.equals("Bool")) {

			reportError(curr.filename, node.lineNo, "Inferred type of predicate in while loop is not Bool: " + T1);
			node.type = "Object";
		} else {
			SemantNode(node.body, c, curr);
			node.type = "Object";
		}
	}

	public void SemantNode(AST.typcase node, ClassTable c, class_ curr) {
		SemantNode(node.predicate, c, curr);
		Set<String> branch_decl = new HashSet<String>();
		List<String> casetypes = new LinkedList<String>();
		for (AST.branch br : node.branches) {

			ArrayList<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();
			newBindings.add(new Pair<String, String>(br.name, br.type));
			Helper.updateObjectEnv(c.objectEnv, curr, newBindings);

			// Checks if declared types of each branch is uqique.
			if (branch_decl.contains(br.type)) {

				reportError(curr.filename, node.lineNo,
						"Duplicate branch " + br.type.toString() + " in case statement.");
				node.type = "Object";
			} else {
				branch_decl.add(br.type);
			}

			// Evaluate the expression, then exit the scope.
			AST.expression e = br.value;
			SemantNode(e, c, curr);
			String caseType = e.type;
			casetypes.add(caseType);
			c.objectEnv.exitScope();
		}
		if (casetypes.isEmpty()) { // If there is no case branch, error.

			reportError(curr.filename, node.lineNo, "There should be at least one branch in cases");
			node.type = "Object";
		} else {
			// Calculates the Least Upper Bound of the case expressions.
			String case_lub = casetypes.remove(0);
			while (!casetypes.isEmpty()) {
				String lub_string = c.inheritanceGraph.lub(case_lub.toString(), casetypes.remove(0).toString(),
						"Object");
				case_lub = c.classNameMapper.get(lub_string).name;
			}
			node.type = case_lub;
		}
	}

	// Let statements with multiple inializations are splitted into multiple let statements. The node.value for each will be an attribute node.
	public void SemantNode(AST.let node, ClassTable c, class_ curr) {

		String T0_prime_string;
		String type_decl = node.typeid;
		T0_prime_string = type_decl;

		if (!(node.value instanceof AST.no_expr)) {
			SemantNode(node.value, c, curr);
			String T1 = node.value.type;
			if (!c.inheritanceGraph.conforms(T1.toString(), T0_prime_string, "Object")) {

				reportError(curr.filename, node.lineNo,
						"Inferred type " + T1.toString() + " does not conform to indentifier " + node.name.toString()
								+ "'s declared type " + T0_prime_string);
			}
		}
		ArrayList<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();
		newBindings.add(new Pair<String, String>(node.name, type_decl));

		Helper.updateObjectEnv(c.objectEnv, curr, newBindings);
		SemantNode(node.body, c, curr);

		node.type = node.body.type;

		c.objectEnv.exitScope();


	}

	public void SemantNode(AST.plus node, ClassTable c, class_ curr) {

		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, node.e1.type + "First Element of plus should be Int");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Element of plus should be Int");
			node.type = "Object";
		}
		node.type = "Int";
	}

	public void SemantNode(AST.sub node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "First Element of sub should be Int");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Element of sub should be Int");
			node.type = "Object";
		}
		node.type = "Int";
	}

	public void SemantNode(AST.mul node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "First Element of mul should be Int");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Element of mul should be Int");
			node.type = "Object";
		}
		node.type = "Int";
	}

	public void SemantNode(AST.divide node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "First Element of divide should be Int");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Element of divide should be Int");
			node.type = "Object";
		}
		node.type = "Int";
	}

	public void SemantNode(AST.neg node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Expr of neg should be Int");
			node.type = "Object";
		}
		node.type = "Int";
	}

	public void SemantNode(AST.lt node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "First Expr of less than should be Int type");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Expr of less than should be Int type");
			node.type = "Object";
		}
		node.type = "Bool";
	}

	public void SemantNode(AST.eq node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		String T1 = node.e1.type;
		SemantNode(node.e2, c, curr);
		String T2 = node.e2.type;

		Set<String> special_case_strings = new HashSet<String>();
		special_case_strings.add("Int");
		special_case_strings.add("String");
		special_case_strings.add("Bool");

		if (special_case_strings.contains(T1.toString()) || special_case_strings.contains(T2.toString())) {
			if (!T1.equals(T2.toString())) {

				reportError(curr.filename, node.lineNo,
						"Int, Bool, and String may only be compared with objects of the same type");
				node.type = "Object";
			} else {
				node.type = "Bool";
			}
		} else {
			node.type = "Bool";
		}
	}

	public void SemantNode(AST.leq node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "First Expr of less than should be Int type");
			node.type = "Object";
		}
		SemantNode(node.e2, c, curr);
		if (!node.e2.type.equals("Int")) {

			reportError(curr.filename, node.lineNo, "Second Expr of less than should be Int type");
			node.type = "Object";
		}
		node.type = "Bool";
	}

	public void SemantNode(AST.comp node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		if (!node.e1.type.equals("Bool")) {

			reportError(curr.filename, node.lineNo, "Expression of not should be Bool type");
			node.type = "Object";
		}
		node.type = "Bool";
	}

	public void SemantNode(AST.int_const node, ClassTable c, class_ curr) {
		node.type = "Int";
	}

	public void SemantNode(AST.bool_const node, ClassTable c, class_ curr) {
		node.type = "Bool";
	}

	public void SemantNode(AST.string_const node, ClassTable c, class_ curr) {
		node.type = "String";
	}

	public void SemantNode(AST.new_ node, ClassTable c, class_ curr) {

		node.type = node.typeid;

	}

	public void SemantNode(AST.isvoid node, ClassTable c, class_ curr) {
		SemantNode(node.e1, c, curr);
		node.type = "Bool";
	}

	public void SemantNode(AST.no_expr node, ClassTable c, class_ curr) {
		node.type = "_no_type";
	}

	public void SemantNode(AST.object node, ClassTable c, class_ curr) {
		Map<String, String> varMap = (Map<String, String>) c.objectEnv.lookUpGlobal(curr.name);
		if (varMap == null) {

			reportError(curr.filename, node.lineNo,
					"Unexpected Error occurred in object, current class not in objectEnv");
			node.type = "Object";
		} else {
			String type = Helper.attrType(node.name, curr, c);
			if (type == null) {

				reportError(curr.filename, node.lineNo,
						"Identifier: " + node.name + " in class " + curr.name.toString() + " is undefined");
				node.type = "Object";
			} else {
				node.type = type;
			}
		}
	}

}

class Helper {
	// Returns the type for an attr name. Checks in the whole class hierarchy.
	public static String attrType(String attrName, class_ curr, ClassTable c) {
		if (c.objectEnv.lookUpGlobal(curr.name) != null
				&& ((Map<String, String>) c.objectEnv.lookUpGlobal(curr.name)).get(attrName) != null)
			return ((Map<String, String>) c.objectEnv.lookUpGlobal(curr.name)).get(attrName);
		Map<String, String> nodeParentMap = c.inheritanceGraph.getNodeParentHashMap("Object");
		String parentName = nodeParentMap.get(curr.name);
		while (parentName != null) {
			class_ parent = c.classNameMapper.get(parentName);
			for (AST.feature f : parent.features) {
				if (f instanceof AST.attr) {
					if (((AST.attr) f).name.equals(attrName.toString()))
						return ((Map<String, String>) c.objectEnv.lookUpGlobal(parent.name)).get(attrName);
				}
			}
			parentName = nodeParentMap.get(parentName);
		}
		return null;
	}

	// Returns the formal list for a method name. Checks in the whole class hierarchy.
	public static List<String> getFormalList(String methodName, class_ curr, ClassTable c) {
		if (c.methodEnv.lookUpGlobal(curr.name) != null
				&& ((Map<String, List<String>>) c.methodEnv.lookUpGlobal(curr.name)).get(methodName) != null)
			return ((Map<String, List<String>>) c.methodEnv.lookUpGlobal(curr.name)).get(methodName);
		Map<String, String> nodeParentMap = c.inheritanceGraph.getNodeParentHashMap("Object");
		String parentName = nodeParentMap.get(curr.name.toString());
		while (parentName != null) {
			class_ parent = c.classNameMapper.get(parentName);
			for (AST.feature f : parent.features) {
				if (f instanceof AST.method) {
					if (((AST.method) f).name.equals(methodName.toString()))
						return ((Map<String, List<String>>) c.methodEnv.lookUpGlobal(parent.name)).get(methodName);
				}
			}
			parentName = nodeParentMap.get(parentName);
		}
		return null;
	}


	//Assumes, class curr's name should be in the SymbolTable otherwise NullPointerException.
	//This method adds a scope the argument SymbolTable.
	public static void updateObjectEnv(ScopeTable<Map<String, String>> objectEnv, class_ curr,
			List<Pair<String, String>> newBindings) {
		//Get current attrTypeMap for the class curr and place all values in newAttrTypeMap.
		Map<String, String> newAttrTypeMap = new HashMap<String, String>();
		newAttrTypeMap.putAll((Map<String, String>) objectEnv.lookUpGlobal(curr.name));

		for (int i = 0; i < newBindings.size(); i++) {
			newAttrTypeMap.put(newBindings.get(i).getLeft(), newBindings.get(i).getRight());
		}

		objectEnv.enterScope();
		objectEnv.insert(curr.name, newAttrTypeMap);
	}

}

//Source: http://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples

class Pair<L, R> {

	private final L left;
	private final R right;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Pair))
			return false;
		Pair pairo = (Pair) o;
		return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
	}

}
