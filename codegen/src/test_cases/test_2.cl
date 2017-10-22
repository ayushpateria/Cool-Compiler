-- Random test case with multiple classes and inheritance

class A {
	aa:Int;
	getA():Int {
		aa
	};
	setA(v:Int):Int {
		aa <- v
	};
};

class B inherits A {
	b:Int;
	a:A <- new A;
	setA(v:Int) : Int {
		{
		a.setA(v);
		while a.getA() < 10 loop { a.setA(a.getA() * 3); } pool;
		a.getA()/3;
		}
	};
	retS() : String {
		"Hi "
	};

};

class Main {
	b:B <- new B;
	v:Int;
	c:String;
	test() : IO {
		new IO.out_string("Hello there!\n")
	};
	main() : IO {
		{
		v <- b.setA(25);
		b <- new B;
		new IO.out_string("Enter: ");
		c <- new IO.in_string();
		new IO.out_string(b.retS().concat(c));
		new IO.out_string("\n");
		new IO.out_int(b.setA(8));
		test();
		}
	};
};


