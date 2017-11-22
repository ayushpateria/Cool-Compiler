-- Error Test Case 4
-- Improper function calls

class Data {
    x : Bool;
    y : Int;
    init(a : Bool, b : Int) : Data {
        {
	    x <- a;
	    y <- b;
	    self;
        }
    };
};


class Main {
    a : Bool;
    main(): Data {
	     {
	         a <- true;
	         (new Data).init(a,10);
           (new Data).init(21,10, false);
           (new Data).init(10, a);
           (new Data).init(a,true);

	     }
    };
};
