-- Error Test Case 5
-- Child class redefining attribute of parent class

class Son inherits Father{
  age : Int;
};

class Father {
  age : Int;
};

class Main {
	main():IO {
		new IO.out_string("Hello world!\n")
	};
};
