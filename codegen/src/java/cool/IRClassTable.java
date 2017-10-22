package cool;

import java.util.*;
import java.util.Map.Entry;

public class IRClassTable {
	private HashMap<String, IRClass> classes = new HashMap<String, IRClass>(); // for retrieving class related info and class attributes and features
	private HashMap<String, Integer> height = new HashMap<String, Integer>(); // for retrieving class height in the inheritance hierarchy (for conformance check)

	public List<Error> errors = new ArrayList<Error>();

	public IRClassTable() {
		/* Classes already present in the table:
		 * - Object
		 * - IO
		 * - String
		 * - Int
		 * - Bool
		 * 
		 * Object has methods:
		 * - abort() : Object
		 * - type_name(): String
		 * IO has methods:
		 * - out_string(x : String) : IO
		 * - out_int(x : Int) : IO
		 * - in_string() : String
		 * - in_int() : String
		 * String has methods:
		 * - length() : Int
		 * - concat(s: String) : String
		 * - substr(i : Int, l : Int) : String
		 */

		List<AST.formal> obj_formal = new ArrayList<AST.formal>();
		obj_formal.add(new AST.formal("this", "Object", 0));

		HashMap<String, AST.method> ol = new HashMap<String, AST.method>();
		ol.put("abort", new AST.method("abort", obj_formal, "Object", new AST.no_expr(0), 0));	

		classes.put("Object", new IRClass("Object", null, new HashMap<String, AST.attr>(), ol));
		height.put("Object", 0);

		HashMap<String, AST.method> iol = new HashMap<String, AST.method>();

		List<AST.formal> os_formals = new ArrayList<AST.formal>();
		List<AST.formal> io_formal = new ArrayList<AST.formal>();
		io_formal.add(new AST.formal("this", "IO", 0));
		os_formals.addAll(io_formal);
		os_formals.add(new AST.formal("out_string", "String", 0));
		List<AST.formal> oi_formals = new ArrayList<AST.formal>();
		oi_formals.addAll(io_formal);
		oi_formals.add(new AST.formal("out_int", "Int", 0));

		iol.putAll(ol);
		iol.put("out_string", new AST.method("out_string", os_formals, "IO", new AST.no_expr(0), 0));
		iol.put("out_int", new AST.method("out_int", oi_formals, "IO", new AST.no_expr(0), 0));
		iol.put("in_string", new AST.method("in_string", io_formal, "String", new AST.no_expr(0), 0));
		iol.put("in_int", new AST.method("in_int", io_formal, "Int", new AST.no_expr(0), 0));

		classes.put("IO", new IRClass("IO", "Object", new HashMap<String, AST.attr>(), iol));
		classes.get("IO").mlist.putAll(ol); // IO inherits from Object
		height.put("IO", 1);


		

		classes.put("Int",
				new IRClass("Int", "Object", new HashMap<String, AST.attr>(), ol));
		height.put("Int", 1);

		classes.put("Bool",
				new IRClass("Bool", "Object", new HashMap<String, AST.attr>(), ol));
		height.put("Bool", 1);

		HashMap<String, AST.method> sl = new HashMap<String, AST.method>();
		List<AST.formal> str_formal = new ArrayList<AST.formal>();
		str_formal.add(new AST.formal("this", "String", 0));
		List<AST.formal> concat_formal = new ArrayList<AST.formal>();
		concat_formal.addAll(str_formal);
		concat_formal.add(new AST.formal("that", "String", 0));
		List<AST.formal> substr_formal = new ArrayList<AST.formal>();
		substr_formal.addAll(str_formal);
		substr_formal.add(new AST.formal("index", "Int", 0));
		substr_formal.add(new AST.formal("len", "Int", 0));

		sl.putAll(ol);
		sl.put("length", new AST.method("length", str_formal, "Int", new AST.no_expr(0), 0));
		sl.put("concat", new AST.method("concat", concat_formal, "String", new AST.no_expr(0), 0));
		sl.put("substr", new AST.method("substr", substr_formal, "String", new AST.no_expr(0), 0));


		classes.put("String", new IRClass("String", "Object", new HashMap<String, AST.attr>(), sl));
		height.put("String", 1);
	
	}

	void insert(AST.class_ c) {
		/* Whenever a new class is inserted,
		 * - Inherits the attributes and methods of the parent class.
		 * - Checks for multiple method or attribute definitions.
		 * - Checks for correct method overrides and any attribute overrides
		 */

		HashMap<String, AST.attr> tc_alist = new HashMap<String, AST.attr>();
		HashMap<String, AST.method> tc_mlist = new HashMap<String, AST.method>();

		IRClass pr = classes.get(c.parent);
		HashMap <String, AST.attr> pr_alist = pr.alist;
		HashMap <String, AST.method> pr_mlist = pr.mlist;
		tc_alist.putAll(pr_alist);
		tc_mlist.putAll(pr_mlist);
		for (Entry<String, AST.method> entry : tc_mlist.entrySet()) {
			List<AST.formal> f = entry.getValue().formals;
			List<AST.formal> new_f = new ArrayList<AST.formal>();
			new_f.addAll(f);
			new_f.set(0, new AST.formal("this", c.name, 0));
			String retType = entry.getValue().typeid;
			if (entry.getValue().typeid.equals(pr.name))
				retType = c.name;
			entry.setValue(new AST.method(entry.getValue().name, new_f, retType, entry.getValue().body, entry.getValue().lineNo));
		}
		for (AST.feature e : c.features) {
			if (e.getClass() == AST.attr.class) {
				AST.attr ae = (AST.attr) e;
				tc_alist.put(ae.name, ae);
			} 
		}
		
		for (AST.feature e : c.features) {
			if (e.getClass() == AST.method.class) {
				AST.method me = (AST.method) e;
				me.formals.add(0, new AST.formal("this", c.name, 0));
				tc_mlist.put(me.name, me);
			}
		}

		IRClass tc = new IRClass(c.name, c.parent, tc_alist, tc_mlist); 

		height.put(c.name, height.get(c.parent) + 1);
		classes.put(c.name, tc);
	}



	List<Error> getErrors() {
		return errors;
	}

	HashMap<String, AST.attr> getAttrs(String className) {
		return classes.get(className).alist;
	}

	IRClass getIRClass(String className) {
		return classes.get(className);
	}

}
