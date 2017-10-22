-- Print factorial

class Main  {
    i : Int;
    j : Int;
    ans : Int;

    fact(i : Int) : Int {
            if i = 0 then 1 else i * fact(i - 1) fi
    };

    main() : IO {
            {
            new IO.out_string("Enter number : ");
            i <- new IO.in_int();
            ans <- fact(i);
            new IO.out_string("Ans : ");
            new IO.out_int(ans);
            new IO.out_string("\n");
            }
    };
};
