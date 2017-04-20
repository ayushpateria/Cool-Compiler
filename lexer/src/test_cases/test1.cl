class Main {
	main():IO {
		new IO.out_string("The new line char is not escaped
         world") (* This should be an error *)

        new IO.out_string("The new line char is escaped and works file \
         world")

        new IO.out_string("More com\plicated \" example \" should work")  

        new IO.out_string("More com\plicated \" example \" 
        Should give error") 
	};
};