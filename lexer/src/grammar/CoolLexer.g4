lexer grammar CoolLexer;

tokens{
	ERROR,
	TYPEID,
	OBJECTID,
	BOOL_CONST,
	INT_CONST,
	STR_CONST,
	LPAREN,
	RPAREN,
	COLON,
	ATSYM,
	SEMICOLON,
	COMMA,
	PLUS,
	MINUS,
	STAR,
	SLASH,
	TILDE,
	LT,
	EQUALS,
	LBRACE,
	RBRACE,
	DOT,
	DARROW,
	LE,
	ASSIGN,
	CLASS,
	ELSE,
	FI,
	IF,
	IN,
	INHERITS,
	LET,
	LOOP,
	POOL,
	THEN,
	WHILE,
	CASE,
	ESAC,
	OF,
	NEW,
	ISVOID,
	NOT
}

/*
  DO NOT EDIT CODE ABOVE THIS LINE
*/

@members{

	/*
		YOU CAN ADD YOUR MEMBER VARIABLES AND METHODS HERE
	*/

	/**
	* Function to report errors.
	* Use this function whenever your lexer encounters any erroneous input
	* DO NOT EDIT THIS FUNCTION
	*/
	public void reportError(String errorString){
		setText(errorString);
		setType(ERROR);
	}

	public void noMatch() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
		reportError(text);
	}

	public void processString() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();

		//write your code to test strings here

		if(text.length() > 1000) {
			reportError("String is too long");
			return;
		}
		String str = "";

		for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '\u0000') 
			{
				reportError("String contains null character");
				return;
			}
			else if(text.charAt(i) == '\\') 
			{
				if (text.charAt(i+1) == 'b')
				{
					str += "\b";
				}
				else if(text.charAt(i+1) == 't')
				{
					str += "\t";
				}
				else if(text.charAt(i+1)== 'n')
				{
					str += "\n";
				}
				else if(text.charAt(i+1) == 'f')
				{
					str += "\f";
				} 			
				else if(text.charAt(i+1) == '"')
				{
					str += '"';
				} 						
				else if(text.charAt(i+1) == '\\')
				{
					str += '\\';
				}
				else 
				{
					str += text.charAt(i+1);
				}
				i++;
			}
			else 
			{
				str += text.charAt(i);
			}
 		}

		setText(str);
		return;
	}

}

/*
	WRITE ALL LEXER RULES BELOW
*/

/** Strings:
 * Unescaped Newline
 * String too long (In processString method)
 * String containing NULL character (In processString method)
 */

STR_CONST   : '"' ( ESC_SEQ | ~('\\'|'"'|'\n'))* '"' { processString();}; // Don't match strings which are having a newline but not escaped.
fragment ESC_SEQ : '\\'.;

// Errors
ERROR		: '"' ~[\n"]* (EOF) { reportError("EOF in string constant"); }
			| '"' (ESC_SEQ | ~["])* '\n' (.*?) '"' { reportError("Unescapted new line in string literal"); }
			| '"' ~["\nEOF]* ('\n') { reportError("Unterminated string constant"); }
			;
SEMICOLON   : ';' ;
DARROW      : '=>' ;
INT_CONST	: '-'?[0-9]+ ;

TYPEID		: [A-Z] [a-zA-Z0-9_]* ;
OBJECTID	: [a-z] [a-zA-Z0-9_]* ;
LE			: '<=';
ASSIGN		: '<-';
PLUS		: '+' ;
MINUS		: '-' ;
STAR		: '*' ;
SLASH		: '/' ;
LPAREN		: '(' ;
RPAREN		: ')' ;
COLON		: ':' ;
RBRACE		: '}' ;
DOT			: '.' ;
TILDE		: '~' ;
LT			: '<' ;
ATSYM		: '@' ;
COMMA		: ',' ;
EQUALS		: '=' ;
LBRACE		: '{' ;
BOOL_CONST	: 't' ('r'|'R') ('u'|'U') ('e'|'E')
			| 'f' ('a'|'A') ('l'|'L') ('s'|'S') ('e'|'E') ;

// Keywords

CLASS		: ('c'|'C') ('l'|'L') ('a'|'A') ('s'|'S') ('s'|'S') ;
ELSE		: ('e'|'E') ('l'|'L') ('s'|'S') ('e'|'E') ;
ISVOID		: ('i'|'I') ('s'|'S') ('v'|'V') ('o'|'O') ('i'|'I') ('d'|'D') ;
IF			: ('i'|'I') ('f'|'F') ;
IN			: ('i'|'I') ('n'|'N') ;
NEW			: ('n'|'N') ('e'|'E') ('w'|'W') ;
FI			: ('f'|'F') ('i'|'I') ;
INHERITS	: ('i'|'I') ('n'|'N') ('h'|'H') ('e'|'E') ('r'|'R') ('i'|'I') ('t'|'T') ('s'|'S') ;
LET			: ('l'|'L') ('e'|'E') ('t'|'T') ;
LOOP		: ('l'|'L') ('o'|'O') ('o'|'O') ('p'|'P') ;
WHILE		: ('w'|'W') ('h'|'H') ('i'|'I') ('l'|'L') ('e'|'E') ;
CASE		: ('c'|'C') ('a'|'A') ('s'|'S') ('e'|'E') ;
POOL		: ('p'|'P') ('o'|'O') ('o'|'O') ('l'|'L') ; 
NOT			: ('n'|'N') ('o'|'O') ('t'|'T') ;
ESAC		: ('e'|'E') ('s'|'S') ('a'|'A') ('c'|'C') ;
OF			: ('o'|'O') ('f'|'F') ;
THEN		: ('t'|'T') ('h'|'H') ('e'|'E') ('n'|'N') ;


WS			: [ \t\r\n\f\v]+ -> skip ;

/* COMMENTS */

LINE_COMMENT: '--' .*? '\n' -> skip ;
END_COMMENT	: '*)' EOF { reportError("Unmatched *)"); } ;
UMCOMMENT 	: '*)' { reportError("Unmatched *)"); } ;
COMMENT		: '(*'-> pushMode(FIRSTCOMMENT), skip;
COMMENTERR	: '(*' EOF { reportError("EOF in comment"); };
 

NOMATCH	: . { noMatch(); } ;

mode FIRSTCOMMENT;
ERR1     	: .(EOF) { reportError("EOF in comment"); } ;
ERR2		: '(*' (EOF) { reportError("EOF in comment"); } ;
STARTCOMMENT	: '(*' -> pushMode(NESTEDC), skip ;
ENDCOMMENT	: '*)' -> popMode, skip ;
CB 			: . -> skip ;

mode NESTEDC;
ERR3		: .(EOF) { reportError("EOF in comment"); } ;
SCOM		: '(*' -> pushMode(NESTEDC), skip ;
ERR4		: '*)' EOF { reportError("EOF in comment"); } ; 
ERR5		: '(*' EOF { reportError("EOF in comment"); } ;
ECOM		: '*)' -> popMode, skip ;
CB1	: . -> skip ;
