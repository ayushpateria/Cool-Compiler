# Cool Language Lexical Analyzer

We analyze a program written in cool and generate tokens from it. Appropriate errors are given incase of lexical errors.

### Design!

The file CoolLexer.g4 contains the rules that are to be followed by the lexer. ANTLR4 will parse the rules in a top down manner, resulting the longest match.
The code is divided in the following:
  - Tokens: List of all the tokens.
  - Members: Contains the methods used to handle strings and errors.
  - Rules: Contains the rules for strings, comments, keywords etc.

### Handling Strings

The following rule matches to the valid string constants 
```
STR_CONST   : '"' ( ESC_SEQ | ~('\\'|'"'|'\n'))* '"' { processString();}; 
fragment ESC_SEQ : '\\'.;
```
Because a string constant cannot contain a single backslash, a single double quotes character or a newline character we match everything inside in a string except those.
Incase we natch a null character in the string the processString method will take care of it.
```
if(text.charAt(i) == '\u0000') 
{
    reportError("String contains null character");
	return;
}
```

### Handling Comments

The thing which needs to be handled with caution here is to make sure all the opening comment tags are properly codes. We have a solution for it using two modes, one for the first comment and the others for subsequent ones.

### Test Cases

The files test_cases contains appropriate amount of test cases.
- test1.cl, test2.cl & test3.cl: These programs demonstrates the errors arising due to null character and  unescaped new line in a string constant. 
- test4.cl : This demonstrate the error due to unclosed comments.
- test5.cl : This is essentially the book example from cool sample programs. It works without any errors. 