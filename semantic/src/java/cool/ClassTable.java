package cool;

import java.io.PrintStream;
import java.util.*;
import cool.AST.class_;

class ClassTable {
	public Graph inheritanceGraph;
	public Map<String, class_> classNameMapper;
	public ScopeTable<Map<String, String>> objectEnv;
	public ScopeTable<Map<String, List<String>>> methodEnv;

	private void installBasicClasses() {
		String filename = "";
		/*
			Object Class:
				- Has no parent class.
				- Basic Methods:
					abort() : Object								Aborts the program and returns the same object.
					type_name() : Str 								Returns the name of the class as a string.
		*/
		List<AST.feature> objm = new ArrayList<AST.feature>();
		objm.add(new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		objm.add(new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		class_ Object_class = new class_("Object", filename, null, objm, 0);
		/*
			IO Class:
				- Inherits from Object class.
				- Basic Methods:
					in_string() : Str						Reads an int from the input.
					in_int() : Int							Reads a string from the input.
					out_string(Str) : SELF_TYPE				Prints a string as the output.
					out_int(Int) : SELF_TYPE 				Prints an int as the output.
		*/
		List<AST.feature> iom = new ArrayList<AST.feature>();
		List<AST.formal> outs_formals = new ArrayList<AST.formal>();
		outs_formals.add(new AST.formal("out_string", "String", 0));
		List<AST.formal> outi_formals = new ArrayList<AST.formal>();
		outi_formals.add(new AST.formal("out_int", "Int", 0));
		iom.add(new AST.method("out_string", outs_formals, "IO", new AST.no_expr(0), 0));
		iom.add(new AST.method("out_int", outi_formals, "IO", new AST.no_expr(0), 0));
		iom.add(new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		iom.add(new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		class_ IO_class = new class_("IO", filename, "Object", iom, 0);
		/*
			Int Class:
				- Has no methods.
				- Only one attribute: 'val' -> For the value of the integer.
		*/
		List<AST.feature> intattr = new ArrayList<AST.feature>();
		intattr.add(new AST.attr("_val", "prim_slot", new AST.no_expr(0), 0));
		class_ Int_class = new class_("Int", filename, "Object", intattr, 0);
		/*
			Bool Class:
				- Has no methods.
				- Only one attribute: 'val' -> For the value of the boolen.
		*/
		List<AST.feature> boolattr = new ArrayList<AST.feature>();
		boolattr.add(new AST.attr("_val", "prim_slot", new AST.no_expr(0), 0));
		class_ Bool_class = new class_("Bool", filename, "Object", boolattr, 0);
		/*
			Str Class:
				- Attributes:
					val												Length of the string.
					str_field										The actual string.
				- Basic Methods:
				 	length() : Int									Returns length of the string.
					concat(Str) : Str								Concatenates strings.
					substr(Int, Int) : Str 							Selects a substring.
		*/
		List<AST.feature> sl = new ArrayList<AST.feature>();
		List<AST.formal> concat_formal = new ArrayList<AST.formal>();
		concat_formal.add(new AST.formal("arg", "String", 0));
		List<AST.formal> substr_formal = new ArrayList<AST.formal>();
		substr_formal.add(new AST.formal("arg", "Int", 0));
		substr_formal.add(new AST.formal("arg2", "Int", 0));

		sl.add(new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		sl.add(new AST.method("concat", concat_formal, "String", new AST.no_expr(0), 0));
		sl.add(new AST.method("substr", substr_formal, "String", new AST.no_expr(0), 0));

		class_ Str_class = new class_("String", filename, "Object", sl, 0);

		classNameMapper.put("Int", Int_class);
		classNameMapper.put("Object", Object_class);
		classNameMapper.put("IO", IO_class);
		classNameMapper.put("String", Str_class);
		classNameMapper.put("Bool", Bool_class);

		for (String className : classNameMapper.keySet()) {
			class_ c1 = classNameMapper.get(className);

			Map<String, List<String>> methodArgMap = new HashMap<String, List<String>>();
			Map<String, String> attrTypeMap = new HashMap<String, String>();

			for (AST.feature f : c1.features) {

				if (f instanceof AST.attr) {
					AST.attr a = (AST.attr) f;
					attrTypeMap.put(a.name, a.typeid);

				} else if (f instanceof AST.method) {
					AST.method m = (AST.method) f;
					List<String> typeList = new ArrayList<String>();

					for (AST.formal fo : m.formals) {
						typeList.add(fo.typeid);
					}
					typeList.add(m.typeid);
					methodArgMap.put(m.name, typeList);

				} else {
					System.out.println("Error should never reach here!");
				}
			}
			methodEnv.insert(c1.name, methodArgMap);
			objectEnv.insert(c1.name, attrTypeMap);

		}

	}

	public ClassTable() {

		inheritanceGraph = new Graph();
		classNameMapper = new HashMap<String, class_>();
		inheritanceGraph.addEdge("Object", "Int");
		inheritanceGraph.addEdge("Object", "String");
		inheritanceGraph.addEdge("Object", "Bool");
		inheritanceGraph.addEdge("Object", "IO");
		objectEnv = new ScopeTable<Map<String, String>>();
		methodEnv = new ScopeTable<Map<String, List<String>>>();
		objectEnv.enterScope();
		methodEnv.enterScope();
		installBasicClasses();

	}

}
