package cool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

public class Constants {

	
	static final String DATA_LAYOUT = "target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"";

	
	static final String TARGET_TRIPLE = "target triple = \"x86_64-unknown-linux-gnu\"";
	
	static final String ATTRIBUTES = "attributes #0 = { nounwind uwtable \"disable-tail-calls\"=\"false\" \"less-precise-fpmad\"=\"false\" \"no-frame-po    inter-elim\"=\"true\" \"no-frame-pointer-elim-non-leaf\" \"no-infs-fp-math\"=\"false\" \"no-nans-fp-math\"=\"false\" \"    stack-protector-buffer-size\"=\"8\" \"target-cpu\"=\"x86-64\" \"target-features\"=\"+fxsr,+mmx,+sse,+sse2\" \"unsafe-    fp-math\"=\"false\" \"use-soft-float\"=\"false\" }\n"
			+ "attributes #1 = { \"disable-tail-calls\"=\"false\" \"less-precise-fpmad\"=\"false\" \"no-frame-pointer-elim\"=\"true    \" \"no-frame-pointer-elim-non-leaf\" \"no-infs-fp-math\"=\"false\" \"no-nans-fp-math\"=\"false\" \"stack-protector-b    uffer-size\"=\"8\" \"target-cpu\"=\"x86-64\" \"target-features\"=\"+fxsr,+mmx,+sse,+sse2\" \"unsafe-fp-math\"=\"false\"     \"use-soft-float\"=\"false\" }";

	static final String CMETHOD_HELPERS = "@strformatstr = private unnamed_addr constant [3 x i8] c\"%s\\00\", align 1\n"
			+ "@intformatstr = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n";
	
	static final String ERRORS = "@Abortdivby0 = private unnamed_addr constant [22 x i8] c\"Error: Division by 0\\0A\\00\", align 1\n"
			+ "@Abortdispvoid = private unnamed_addr constant [25 x i8] c\"Error: Dispatch on void\\0A\\00\", align 1\n";
	
	static final String CMETHODS = "declare i32 @printf(i8*, ...)\n"
			+ "declare i32 @scanf(i8*, ...)\n"
			+ "declare i32 @strcmp(i8*, i8*)\n"
			+ "declare i8* @strcat(i8*, i8*)\n"
			+ "declare i8* @strcpy(i8*, i8*)\n"
			+ "declare i8* @strncpy(i8*, i8*, i32)\n"
			+ "declare i64 @strlen(i8*)\n"
			+ "declare i8* @malloc(i64)\n"
			+ "declare void @exit(i32)";
	

		
	public static final Map<String, String> COOL_TYPES;
	static
    {
        COOL_TYPES = new HashMap<String, String>();
		COOL_TYPES.put("Int", "i32");
		COOL_TYPES.put("Bool", "i32");
		COOL_TYPES.put("String", "[1024 x i8]");
    }

	public static final List<String> BasicMethods = Arrays.asList("ABORT", "CONCAT", "TYPE_NAME", "LENGTH", "MEMCPY", "SUBSTR", "COPY", "OUT_INT", "OUT_STRING", "IN_STRING", "IN_INT");

	public static String getMethod(String mName, IRClass cl){
		Map<String, String> METHODS = new HashMap<String, String>();
			METHODS.put("ABORT", "define %class." + cl.name + "* @"+ cl.name +"_abort( %class."+ cl.name +"* %this ) noreturn {\n"
			+ "entry:\n"
			+ "call void @exit( i32 1 )\n"
			+ "ret %class."+ cl.name +"* null\n"
			+ "}\n");

			METHODS.put("TYPE_NAME", "define [1024 x i8]* @"+ cl.name +"_type_name( %class."+ cl.name +"* %this ) {\n"
			+ "entry:\n"
			+ "%0 = getelementptr inbounds %class."+ cl.name +", %class."+ cl.name +"* %this, i32 0, i32 0\n"
			+ "%1 = load i32, i32* %0\n"
			+ "%2 = getelementptr inbounds [8 x [1024 x i8]], [8 x [1024 x i8]]* @classnames, i32 0, i32 %1\n"
			+ "%retval = call [1024 x i8]* @"+ cl.name +"copy( [1024 x i8]* %2 )\n"
			+ "ret [1024 x i8]* %retval\n"
			+ "}\n");
	
			METHODS.put("CONCAT", "define [1024 x i8] @"+ cl.name +"_concat( [1024 x i8] %this_, [1024 x i8] %that_ ) {\n"
			+ "entry:\n"
			+ "%this = alloca [1024 x i8], align 16\n"
			+ "store [1024 x i8] %this_, [1024 x i8]* %this, align 16\n"
			+ "%that = alloca [1024 x i8], align 16\n"
			+ "store [1024 x i8] %that_, [1024 x i8]* %that, align 16\n"
			+ "%retval = call [1024 x i8]* @"+ cl.name +"_copy( [1024 x i8]* %this )\n"
			+ "%0 = bitcast [1024 x i8]* %retval to i8*\n"
			+ "%1 = bitcast [1024 x i8]* %that to i8*\n"
			+ "%2 = call i8* @strcat( i8* %0, i8* %1 )\n"
			+ "%3 = load [1024 x i8], [1024 x i8]* %retval\n"
			+ "ret [1024 x i8] %3\n"
			+ "}\n");
	
			METHODS.put("LENGTH", "define i32 @"+ cl.name +"_length( [1024 x i8] %this_ ) {\n"
			+ "entry:\n"
			+ "%this = alloca [1024 x i8], align 16\n"
			+ "store [1024 x i8] %this_, [1024 x i8]* %this, align 16\n"
			+ "%0 = bitcast [1024 x i8]* %this to i8*\n"
			+ "%1 = call i64 @strlen( i8* %0 )\n"
			+ "%retval = trunc i64 %1 to i32\n"
			+ "ret i32 %retval\n"
			+ "}\n");
	
			METHODS.put("SUBSTR", "define [1024 x i8] @"+ cl.name +"_substr( [1024 x i8] %this_, i32 %start, i32 %len ) {\n"
			+ "entry:\n"
			+ "%this = alloca [1024 x i8], align 16\n"
			+ "store [1024 x i8] %this_, [1024 x i8]* %this, align 16\n"
			+ "%0 = getelementptr inbounds [1024 x i8], [1024 x i8]* %this, i32 0, i32 %start\n"
			+ "%1 = call i8* @malloc( i64 1024 )\n"
			+ "%retval = bitcast i8* %1 to [1024 x i8]*\n"
			+ "%2 = bitcast [1024 x i8]* %retval to i8*\n"
			+ "%3 = call i8* @strncpy( i8* %2, i8* %0, i32 %len )\n"
			+ "%4 = getelementptr inbounds [1024 x i8], [1024 x i8]* %retval, i32 0, i32 %len\n"
			+ "store i8 0, i8* %4\n"
			+ "%5 = load [1024 x i8], [1024 x i8]* %retval\n"
			+ "ret [1024 x i8] %5\n"
			+ "}\n");

			
			METHODS.put("OUT_STRING", "define %class."+ cl.name +"* @"+ cl.name +"_out_string( %class."+ cl.name +"* %this, [1024 x i8] %str ) {\n"
			+ "entry:\n"
			+ "%0 = alloca [1024 x i8], align 16\n"
			+ "store [1024 x i8] %str, [1024 x i8]* %0, align 16\n"
			+ "%1 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %0 )\n"
			+ "ret %class."+ cl.name +"* %this\n"
			+ "}\n");
	
			METHODS.put("OUT_INT", "define %class."+ cl.name +"* @"+ cl.name +"_out_int( %class."+ cl.name +"* %this, i32 %int ) {\n"
			+ "entry:\n"
			+ "%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )\n"
			+ "ret %class."+ cl.name +"* %this\n"
			+ "}\n");
	
			METHODS.put("IN_STRING", "define [1024 x i8] @"+ cl.name +"_in_string( %class."+ cl.name +"* %this ) {\n"
			+ "entry:\n"
			+ "%0 = call i8* @malloc( i64 1024 )\n"
			+ "%retval = bitcast i8* %0 to [1024 x i8]*\n"
			+ "%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %retval )\n"
			+ "%2 = load [1024 x i8], [1024 x i8]* %retval\n"
			+ "ret [1024 x i8] %2\n"
			+ "}\n");
	
			METHODS.put("IN_INT", "define i32 @"+ cl.name +"_in_int( %class."+ cl.name +"* %this ) {\n"
			+ "entry:\n"
			+ "%0 = call i8* @malloc( i64 4 )\n"
			+ "%1 = bitcast i8* %0 to i32*\n"
			+ "%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )\n"
			+ "%retval = load i32, i32* %1\n"
			+ "ret i32 %retval\n"
			+ "}\n");


			if (cl.name.equals("String")) 
			{
					
			METHODS.put("COPY", "define [1024 x i8]* @"+ cl.name +"_copy( [1024 x i8]* %this ) {\n"
			+ "entry:\n"
			+ "%0 = call i8* @malloc( i64 1024 )\n"
			+ "%retval = bitcast i8* %0 to [1024 x i8]*\n"
			+ "%1 = bitcast [1024 x i8]* %this to i8*\n"
			+ "%2 = bitcast [1024 x i8]* %retval to i8*\n"
			+ "%3 = call i8* @strcpy( i8* %2, i8* %1)\n"
			+ "ret [1024 x i8]* %retval\n"
			+ "}\n");

			}

			return METHODS.get(mName);
			
	}

}
