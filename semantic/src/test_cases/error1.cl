-- Error Test Case 1
-- Improper Hierarchy and Cyclic Inheritance

class Son inherits Father{
	dad : Father <- new Father;
};

class Father inherits Son {
	kid : Son <- new Son;
};

class X inherits Y {
};
class Y inherits Z {
};
class Z inherits X {
};


class Main {
	main():IO {
		new IO.out_string("Hello world!\n")
	};
};
