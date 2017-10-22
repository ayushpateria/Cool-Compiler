package cool;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;




public class Codegen{
	
	IRClassTable IRclassTable;
	ArrayList < ArrayList <Integer> > classGraph;
	List<String> globalStr = new ArrayList<String>();
	HashMap <Integer, String> idxName;
	HashMap <String, AST.class_> idxCont;
	int regNo;
	int strCount = 1;
	
	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
        out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
       
        out.println(Constants.DATA_LAYOUT);
		out.println(Constants.TARGET_TRIPLE);
        
        IRclassTable = new IRClassTable();
        ProcessGraph(program.classes, out);

		printMainMethod(out);
		
		for(String s : globalStr) {
			out.println(s);
		}


		out.println(Constants.ATTRIBUTES);
		out.println(Constants.CMETHODS);
		out.println(Constants.CMETHOD_HELPERS);
		out.println(Constants.ERRORS);	
		out.println();	
		
	}

	private void printMainMethod(PrintWriter out) {
		String mType = IRType(IRclassTable.getIRClass("Main").mlist.get("main").typeid);
		out.println("define void @main() #0 {\n"
					+ "%1 = alloca %class.Main, align 4 \n");
		int rno = 1;
		String cname = "Main";
		for (Entry<String, AST.attr> attrEntry : IRclassTable.getAttrs(cname).entrySet()) {
			AST.attr attrNode = attrEntry.getValue();
			if (attrNode.value.getClass() == AST.new_.class) {
				AST.new_ node = (AST.new_)attrNode.value;
				out.println("%"+ ++rno+" = alloca " + IRType2(node.typeid)+ ", align 4");
				out.println("%"+ ++rno +" = getelementptr inbounds %class." + cname + ", %class."+cname+"* %1, i32 0, i32 "+IRclassTable.getIRClass(cname).attrOffset.get(attrNode.name));
				out.println("store " + IRType2(node.typeid)+ "* %"+ (rno-1) +", " + IRType2(node.typeid)+ "** %"+rno+", align 8");		
			}
		}	

		out.println("%"+ ++rno +" = call " + mType + " @Main_main(%class.Main* %1)\n"
					+ "ret void\n}\n");
	}

	private void defineStringConst(AST.expression expr, PrintWriter out)
	{
		AST.string_const str_obj = (AST.string_const)expr;	
		globalStr.add("@.str."+ strCount++ + " = private unnamed_addr constant ["+ (str_obj.value.length()+1) + " x i8] c\""+str_obj.value+"\\00\", align 1");
	}


	

	private void ProcessMethod(IRClass irclass, HashMap <String, AST.formal> fmap, AST.expression expr, PrintWriter out, boolean lastExpr)
	{
		
		// assign
		if(expr.getClass() == AST.assign.class)
		{
			String reg, varType;
			AST.assign str = (AST.assign)expr;
			AST.expression exp = str.e1;
			if(irclass.alist.containsKey(str.name)) {
				out.println("%"+ ++regNo +" = getelementptr inbounds %class." + irclass.name + ", %class."+irclass.name+"* %this, i32 0, i32 "+irclass.attrOffset.get(str.name));
				reg = "%"+regNo;
				varType = IRType(irclass.alist.get(str.name).typeid);
			}
			else { // it must be defined in formals.
				reg = "%"+str.name;
				varType = IRType(fmap.get(str.name).typeid);
			}

			ProcessMethod(irclass, fmap, exp, out, false);

			out.println("store " + varType + " %" + regNo + ", " + varType + "* "+ reg +" , align 4");

			if(lastExpr)
				out.println("ret "+varType + " %" + regNo);
		}

		// plus
		if(expr.getClass() == AST.plus.class)
		{
			AST.plus node = (AST.plus)expr;
			ProcessMethod(irclass, fmap, node.e1, out, false);
			String reg1 = "%"+(regNo);
			ProcessMethod(irclass, fmap, node.e2, out, false);
			String reg2 = "%"+(regNo);
			out.println("%"+ ++regNo +" = add nsw i32 " + reg1 + ", " + reg2);


			if(lastExpr)
				out.println("ret "+IRType(node.e1.type) + " %" + regNo);

		}

		// sub
		if(expr.getClass() == AST.sub.class)
		{
			AST.sub node = (AST.sub)expr;
			ProcessMethod(irclass, fmap, node.e1, out, false);
			String reg1 = "%"+(regNo);
			ProcessMethod(irclass, fmap, node.e2, out, false);
			String reg2 = "%"+(regNo);
			out.println("%"+ ++regNo +" = sub nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr)
				out.println("ret "+IRType(node.e1.type) + " %" + regNo);
		}

		// mul
		if(expr.getClass() == AST.mul.class)
		{
			AST.mul node = (AST.mul)expr;
			ProcessMethod(irclass, fmap, node.e1, out, false);
			String reg1 = "%"+(regNo);
			ProcessMethod(irclass, fmap, node.e2, out, false);
			String reg2 = "%"+(regNo);
			out.println("%"+ ++regNo +" = mul nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr)
				out.println("ret "+IRType(node.e1.type) + " %" + regNo);
		}



		// div
		if(expr.getClass() == AST.divide.class)
		{
			AST.divide node = (AST.divide)expr;
			ProcessMethod(irclass, fmap, node.e1, out, false);
			String reg1 = "%"+(regNo);
			ProcessMethod(irclass, fmap, node.e2, out, false);
			String reg2 = "%"+(regNo);
			out.println("%"+ ++regNo +" = sdiv i32 " + reg1 + ", " + reg2);

			if(lastExpr)
				out.println("ret "+IRType(node.e1.type) + " %" + regNo);
		}

		// int
		if(expr.getClass() == AST.int_const.class)
		{
			out.println("%"+ ++regNo +" = alloca i32, align 4");
			out.println("store i32 "+((AST.int_const)expr).value+", i32* %"+regNo+", align 4");
			out.println("%"+ ++regNo + " = load i32, i32* %"+ (regNo-1) + ", align 4");

			if(lastExpr)
				out.println("ret i32 %" + regNo);
		}

		// bool
		if(expr.getClass() == AST.bool_const.class)
		{
			boolean b = ((AST.bool_const)expr).value;
			int v = 0;
			if (b) 
				v = 1; 
			out.println("%"+ ++regNo +" = alloca i32, align 1");
			out.println("store i32 "+v+", i32* %"+regNo+", align 4");
			out.println("%"+ ++regNo + " = load i32, i32* %"+ (regNo-1) + ", align 4");

			if(lastExpr)
				out.println("ret int32 %" + regNo);
		}

		// new
		if(expr.getClass() == AST.new_.class)
		{
			AST.new_ node = (AST.new_)expr;
			out.println("%"+ ++regNo +" = alloca " + IRType2(node.typeid)+ ", align 4");
		
			if(lastExpr)
				out.println("ret " + IRType2(node.typeid)+ "* %" + regNo);
		}


		// ID
		if(expr.getClass() == AST.object.class)
		{
			String reg;
			AST.object str = (AST.object)expr;
			if (str.name.equals("self")) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %this, "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}

			else if(fmap.containsKey(str.name)) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %"+str.name+", "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}
			else { // it must be defined in attributes.
				out.println("%"+ ++regNo +" = getelementptr inbounds %class." + irclass.name + ", %class."+irclass.name+"* %this, i32 0, i32 "+irclass.attrOffset.get(str.name));
				out.println("%"+ ++regNo +" = load  " + IRType(irclass.alist.get(str.name).typeid) + ", "+ IRType(irclass.alist.get(str.name).typeid) + "* %"+(regNo-1)+", align 4");
		
			}

			if(lastExpr)
				out.println("ret "+IRType(str.type) + " %" + regNo);
		}



		// dispatch
		else if(expr.getClass() == AST.dispatch.class)
		{
			AST.dispatch str = (AST.dispatch)expr;   
			if( !(str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) )
				ProcessMethod(irclass, fmap, str.caller, out, false);
			String thisReg = ""+regNo;

			String caller = str.caller.type;
			if( (str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) ) {
				caller = irclass.name;
				thisReg = "this";
			} else {

				// Go through the attrs of the class called, and allocate if there's an initialisation in the attributes node.

				String cname = caller;
				for (Entry<String, AST.attr> attrEntry : IRclassTable.getAttrs(cname).entrySet()) {
					AST.attr attrNode = attrEntry.getValue();
					if (attrNode.value.getClass() == AST.new_.class) {
						AST.new_ node = (AST.new_)attrNode.value;
						out.println("%"+ ++regNo +" = alloca " + IRType2(node.typeid)+ ", align 4");
						out.println("%"+ ++regNo  +" = getelementptr inbounds %class." + cname + ", %class."+cname+"* %" + thisReg + ", i32 0, i32 "+IRclassTable.getIRClass(cname).attrOffset.get(attrNode.name));
						out.println("store " + IRType2(node.typeid)+ "* %"+ (regNo - 1) +", " + IRType2(node.typeid)+ "** %"+regNo+", align 8");		
					}

				}	
			}

			String methodName = str.name;
			String mType =   IRType(IRclassTable.getIRClass(caller).mlist.get(methodName).typeid);
			String dispatchStr = "call " + mType + " @"+caller+"_"+methodName + "("+ IRType(caller) + " %"+thisReg;
		
			for(int i = 0; i < str.actuals.size(); i++)
			{
				expr = str.actuals.get(i);
				ProcessMethod(irclass, fmap, expr, out, false);
				dispatchStr += ", "+IRType(expr.type) + " %"+ regNo;
			}               

			dispatchStr += ")";

			out.println("%"+ ++regNo + " = " + dispatchStr);

			if (lastExpr)
				out.println("ret "+ mType + " %" + regNo);
		}


		// if then else
		else if(expr.getClass() == AST.cond.class)
		{
			AST.cond str = (AST.cond)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			ProcessMethod(irclass, fmap, e1, out, false);
			String tag = ""+regNo;			
			out.println("br i1 %"+regNo+", label %ifbody"+tag+", label %elsebody"+tag);
		
			out.println("ifbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);
			if(lastExpr) 
				out.println("ret "+IRType(e2.type) + " %" + regNo++);
			out.println("br label %elsebody"+tag);
			
			out.println("elsebody"+tag+":");
			ProcessMethod(irclass, fmap, e3, out, false);
			if(lastExpr)
				out.println("ret "+IRType(e3.type) + " %" + regNo++);		
		
		    out.println("br label %thenbody"+tag);
			out.println("thenbody"+tag+":");
			
			if(lastExpr) 
			out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %"+(regNo-1));
			
		}


		// block
		else if(expr.getClass() == AST.block.class)
		{
			AST.block str = (AST.block)expr;
			List<AST.expression> listExp = new ArrayList<AST.expression>();
			listExp = str.l1;
			boolean isMethod = false;
			if (lastExpr == true) 
				isMethod = true;
			lastExpr = false;
			for(int i = 0; i < listExp.size(); ++i)
	        {
	        	AST.expression e2 = new AST.expression();
				e2 = listExp.get(i);
				if (i == listExp.size()-1 && isMethod)
					lastExpr = true;

				ProcessMethod(irclass, fmap, e2, out, lastExpr);

	        }

		}


		// while loop
		else if(expr.getClass() == AST.loop.class)
		{
			AST.loop str = (AST.loop)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.body;
			
			String tag = ""+regNo;						
			out.println("br label %"+"predicate"+tag);
			out.println("predicate"+tag+":");
			ProcessMethod(irclass, fmap, e1, out, false);
			out.println("br i1 %"+regNo+", label %loopbody"+tag+", label %pool"+tag);

			out.println("loopbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);			
			out.println("br label %predicate"+tag);

			out.println("pool"+tag+":");

			if(lastExpr)
				out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %" + regNo);

		}

		// equal to
		else if(expr.getClass() == AST.eq.class)
		{
			AST.eq str = (AST.eq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp eq " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than
		else if(expr.getClass() == AST.lt.class)
		{
			AST.lt str = (AST.lt)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp slt " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than equal to
		else if(expr.getClass() == AST.leq.class)
		{
			AST.leq str = (AST.leq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp sle " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// negation (not EXPR)
		else if(expr.getClass() == AST.neg.class)
		{
			AST.neg str = (AST.neg)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp ne " + IRType(str.e1.type) + " "+reg1+", "+reg1);
		}

		// string const
		else if(expr.getClass() == AST.string_const.class)
		{
			AST.string_const str = (AST.string_const)expr;
			defineStringConst(str,out);
			out.println("%"+ ++regNo +" = alloca [1024 x i8], align 16");
			out.println("%"+ ++regNo +" = getelementptr inbounds [1024 x i8], [1024 x i8]* %"+(regNo-1)+", i32 0, i32 0");
			out.println("%"+ ++regNo +" = call i8* @strcpy(i8* %"+ (regNo-1) + ", i8* getelementptr inbounds (["+ (str.value.length()+1) +" x i8], ["+ (str.value.length()+1) +" x i8]* @.str."+ (strCount-1) +", i32 0, i32 0))");		
			out.println("%"+ ++regNo +" = load [1024 x i8], [1024 x i8]* %"+(regNo-3) + ", align 16");		
			
			if(lastExpr) {
				out.println("ret "+IRType(str.type) + " %" + (regNo));
			}
		}
	}
	
	
	// given a method, get its type signature
	private String IRType(String typeid) {
		if(Constants.COOL_TYPES.containsKey(typeid)) {
			return Constants.COOL_TYPES.get(typeid);
		}
		else return "%class." + typeid + "*";
	}

	private String IRType2(String typeid) {
		if(Constants.COOL_TYPES.containsKey(typeid)) {
			return Constants.COOL_TYPES.get(typeid);
		}
		else return "%class." + typeid;
	}
	
	private void EmitClassDecl(IRClass irclass, PrintWriter out) {
		out.print("%class." + irclass.name + " = type {");
		int i = 0;
		for (Entry<String, AST.attr> entry : irclass.alist.entrySet()) {
			AST.attr at = entry.getValue();
			if(i != 0)
				out.print(", ");
			out.print(IRType(at.typeid));
			i++;
		}
		out.print(" }\n");
	}


	/* A constructor for all base types is required */
	
	private void EmitMethods(IRClass irclass, PrintWriter out) {
		
		if(irclass.name.equals("Int") || irclass.name.equals("Bool")) return;
		else if(irclass.name.equals("String")) {
			out.println(Constants.getMethod("CONCAT", irclass));
			out.println(Constants.getMethod("COPY", irclass));
			out.println(Constants.getMethod("LENGTH", irclass));
			out.println(Constants.getMethod("SUBSTR", irclass));
		} else if(irclass.name.equals("Object")) {
			out.println(Constants.getMethod("ABORT", irclass));
		} else if(irclass.name.equals("IO")) {
			out.println(Constants.getMethod("OUT_STRING", irclass));
			out.println(Constants.getMethod("IN_STRING", irclass));
			out.println(Constants.getMethod("OUT_INT", irclass));
			out.println(Constants.getMethod("IN_INT", irclass));
		} else {
			HashMap <String, AST.formal> fmap = new HashMap <String, AST.formal>();
			// Generate the code for the Method here.
			for (Entry<String, AST.method> entry : irclass.mlist.entrySet()) {
				String mname = entry.getKey();
				AST.method me = entry.getValue();

				if (Constants.BasicMethods.contains(me.name.toUpperCase())) {
					out.println(Constants.getMethod(me.name.toUpperCase(), irclass));
					continue;
				}

				out.print("define " + IRType(me.typeid) + " "+ irclass.IRname.get(me.name) + "(");
				for(int i = 0; i < me.formals.size(); i++) {
					if (i != 0)
						out.print(", ");
					out.print(IRType(me.formals.get(i).typeid) + " %" + me.formals.get(i).name);
					fmap.put(me.formals.get(i).name, me.formals.get(i));
				}
				fmap.put("#rettype", new AST.formal("ret", me.typeid, 0));	// To know return type of method. Used for while loop.
				out.println(") #0 {");
				// print method body
				regNo = 0;
				ProcessMethod(irclass, fmap, me.body, out, true);

				out.println("}\n");

			}
		}
	}
	
	
	// Modified version of ProcessGraph in Semantic.java. Traverses classes in BFS order, so that 
	// the parent methods and attributes are defined already.
	private void ProcessGraph(List <AST.class_> classes, PrintWriter out) {
			
	
		Integer sz = 0;		// stores the number of classes
		idxCont = new HashMap <String, AST.class_> ();
		HashMap <String, Integer> classIdx = new HashMap <String, Integer> ();
		idxName = new HashMap <Integer, String>();
		classGraph = new ArrayList < ArrayList <Integer> >();

		/* Laying the groundwork */
		classIdx.put("Object", 0);
		idxName.put(0, "Object");
		classIdx.put("IO", 1);
		idxName.put(0, "IO");
	
		classIdx.put("String", 2);
		idxName.put(0, "String");
		
		classGraph.add(new ArrayList <Integer> (Arrays.asList(1, 2)));
		classGraph.add(new ArrayList <Integer>());	// for IO
		classGraph.add(new ArrayList <Integer>());	// for String		
		idxName.put(0, "Object");
		idxName.put(1, "IO");
		idxName.put(2, "String");
		
		sz = sz + 3;	// IO and Object (2 classes) already present
		
		/* Checking for :
		 * - bad redefinitions
		 * - bad inheritance
		 * Also : assigning an integer corresponding to each class.
		 */
		for(AST.class_ e : classes) {
			idxName.put(sz, e.name);			// Reverse lookup. Integer -> className
			classIdx.put(e.name, sz++);			// className -> Integer
			idxCont.put(e.name, e);				// getting the class from name. Used later.
			classGraph.add(new ArrayList <Integer> ());
		}


		
		/* We are creating an undirected graph in this method.
		 * Also: Checking for - undefined parent class error
		 */
		for(AST.class_ e : classes) {
			int u = classIdx.get(e.parent);
			int v = classIdx.get(e.name);
			classGraph.get(u).add(v);			// adding an edge from parent -> child in the graph
		}
		
		
		/* Class Declarations here */
		Queue<Integer> q = new LinkedList<Integer>();
		q.clear(); q.offer(0);
		while (!q.isEmpty()) {
			int u = q.poll();
			if(u != 2  && u != 1 && u != 0) {
				IRclassTable.insert(idxCont.get(idxName.get(u)));		// insert classes in BFS-order so that methods and attributes can be inherited.
			}
			EmitClassDecl(IRclassTable.getIRClass(idxName.get(u)), out);
			for(Integer v : classGraph.get(u)) {
				q.offer(v);
			}
		}


 
		/* prints definitions */
		q.clear(); q.offer(0);
		while (!q.isEmpty()) {
			int u = q.poll();
			EmitMethods(IRclassTable.getIRClass(idxName.get(u)), out);
			for(Integer v : classGraph.get(u)) {
				q.offer(v);
			}
		
		}

		
	}

}