-- Error Test Case 3
-- Undefined class and type names

class Data {
    x : Bool;
    y : Potato;
    z : Int;
    init(a : Potato, b : Pineapple, c : Int ) : Data {
        {
	    x <- a;
	    z <- c;
	    self;
        }
    };
};



Class Main {
    a : Bool;
    b : Potato;
    c : Int;
    main() : C {
	{
	    a <- true;
      b <- "Hot Potato";
      c <- 20;
	    (new Data).init(a,b, c);
	}
    };
};
