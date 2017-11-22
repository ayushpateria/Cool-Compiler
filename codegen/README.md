CS3423: Mini Assignment
=================

Code Generation Report
-------------------------

Code generation is the next step after having done semantic analysis. This assignment broadly involved emitting equivalent LLVM IR code for the given Cool code. The main files in the project are `Codegen.java`, `IRClassTable.java`, `IRClass.java` and `Constants.java`.


Design Decisions :
==================

Since there is no concept of *Class* in LLVM IR, we use the type struct to represent the classes. Every class in the cool program has a corresponding struct in the IR code, with the members being same as the attributes of the class. The methods of the class are defined globally, following the naming convention: `ClassName_MethodName`. The first argument to the methods is the `%this` pointer, which holds the reference to the corresponding struct.

Attributes in Cool classes have to mapped to the offsets in the LLVM IR struct, also we need to store the method names in the IR somewhere. For this purpose, we introduce the class `IRClass`, it does the bookkeeping work for offsets and methodnames. 

All the `IRClass`es are stored in a HashMap in *IRClassTable.java* class, where we also install the basic classes. To handle inheritance, we simply include the attributes and methods of the parent class in the child class. The `insert()` function in *IRClassTable* takes care of this.

*Codegen.java* contains the Codegen method, which is the entry point to the code generator. Since, while inserting IRClasses in IRClassTable we use parents attributes and methods, it is important to ensure that we traverse the classes in a BFS or DFS manner. The `ProcessGraph()` function traverses over the classes in a BFS manner, and emits Class and Method declaration codes. The main code generation is done in the recursive `ProcessMethod` calls. 

*Constants.java* mainly contains the code for the methods of Object, String and IO. These are implemented using system call interfaces.


### Structure of ProcessMethod

The ProcessMethod is called upon every expression in method body. It checks for what type of node we are generating the code, and emits the IR. We use the `regNo` counter, which is incremented on every instruction. After a ProcessMethod returns, we assume that the value of that node is stored in the last count of `regNo`. So, for an expression like a <- 2 + 3, we store the int constants 2 and 3 in a register using two ProcessMethod calls and add them using the corresponding `regNo`s.

## Handling Strings

The strings are stored as the data type `[1024 x i8]` in the struct. To assign a new value to a string attribute, we use the `@strcpy` call. All string constants are stored in a List and declared globally at the end of IR. This is done in `defineStringConst()`.


**IMPORTANT**

- The code is tested in an Ubuntu 64 bit machine and works fine on the test cases.
- Since SELF_TYPE and dynamic dispatch are not supported, while testing the code generator, please don't have a method like this:

	class Main inherits IO {
		
		main() : IO {
			in_string()
		};
	};

    This wouldn't work! Because this means we are returning an object of type main and are expecting return of an IO object. The return type and the type of object returned have to be exactly same! However, changing IO to Main in the method return type should be fine (But it gave me a semantic error with the implementaion given! while origninal coolc runs it fine).
