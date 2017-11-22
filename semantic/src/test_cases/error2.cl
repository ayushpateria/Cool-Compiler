-- Error Test Case 2
-- Lack of a 'main' menthod

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
    mainasdf(): Data {
	     {
	         a <- true;
	         (new Data).init(a,10);
	     }
    };
};
