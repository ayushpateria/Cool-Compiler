// Generated from CoolLexer.g4 by ANTLR 4.5
package cool;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, TYPEID=2, OBJECTID=3, BOOL_CONST=4, INT_CONST=5, STR_CONST=6, 
		LPAREN=7, RPAREN=8, COLON=9, ATSYM=10, SEMICOLON=11, COMMA=12, PLUS=13, 
		MINUS=14, STAR=15, SLASH=16, TILDE=17, LT=18, EQUALS=19, LBRACE=20, RBRACE=21, 
		DOT=22, DARROW=23, LE=24, ASSIGN=25, CLASS=26, ELSE=27, FI=28, IF=29, 
		IN=30, INHERITS=31, LET=32, LOOP=33, POOL=34, THEN=35, WHILE=36, CASE=37, 
		ESAC=38, OF=39, NEW=40, ISVOID=41, NOT=42, WS=43, LINE_COMMENT=44, END_COMMENT=45, 
		UMCOMMENT=46, COMMENT=47, COMMENTERR=48, NOMATCH=49, ERR1=50, ERR2=51, 
		STARTCOMMENT=52, ENDCOMMENT=53, CB=54, ERR3=55, SCOM=56, ERR4=57, ERR5=58, 
		ECOM=59, CB1=60;
	public static final int FIRSTCOMMENT = 1;
	public static final int NESTEDC = 2;
	public static String[] modeNames = {
		"DEFAULT_MODE", "FIRSTCOMMENT", "NESTEDC"
	};

	public static final String[] ruleNames = {
		"STR_CONST", "ESC_SEQ", "ERROR", "SEMICOLON", "DARROW", "INT_CONST", "TYPEID", 
		"OBJECTID", "LE", "ASSIGN", "PLUS", "MINUS", "STAR", "SLASH", "LPAREN", 
		"RPAREN", "COLON", "RBRACE", "DOT", "TILDE", "LT", "ATSYM", "COMMA", "EQUALS", 
		"LBRACE", "BOOL_CONST", "CLASS", "ELSE", "ISVOID", "IF", "IN", "NEW", 
		"FI", "INHERITS", "LET", "LOOP", "WHILE", "CASE", "POOL", "NOT", "ESAC", 
		"OF", "THEN", "WS", "LINE_COMMENT", "END_COMMENT", "UMCOMMENT", "COMMENT", 
		"COMMENTERR", "NOMATCH", "ERR1", "ERR2", "STARTCOMMENT", "ENDCOMMENT", 
		"CB", "ERR3", "SCOM", "ERR4", "ERR5", "ECOM", "CB1"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, "'('", "')'", "':'", "'@'", 
		"';'", "','", "'+'", "'-'", "'*'", "'/'", "'~'", "'<'", "'='", "'{'", 
		"'}'", "'.'", "'=>'", "'<='", "'<-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "ERROR", "TYPEID", "OBJECTID", "BOOL_CONST", "INT_CONST", "STR_CONST", 
		"LPAREN", "RPAREN", "COLON", "ATSYM", "SEMICOLON", "COMMA", "PLUS", "MINUS", 
		"STAR", "SLASH", "TILDE", "LT", "EQUALS", "LBRACE", "RBRACE", "DOT", "DARROW", 
		"LE", "ASSIGN", "CLASS", "ELSE", "FI", "IF", "IN", "INHERITS", "LET", 
		"LOOP", "POOL", "THEN", "WHILE", "CASE", "ESAC", "OF", "NEW", "ISVOID", 
		"NOT", "WS", "LINE_COMMENT", "END_COMMENT", "UMCOMMENT", "COMMENT", "COMMENTERR", 
		"NOMATCH", "ERR1", "ERR2", "STARTCOMMENT", "ENDCOMMENT", "CB", "ERR3", 
		"SCOM", "ERR4", "ERR5", "ECOM", "CB1"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}



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



	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0:
			STR_CONST_action((RuleContext)_localctx, actionIndex);
			break;
		case 2:
			ERROR_action((RuleContext)_localctx, actionIndex);
			break;
		case 45:
			END_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 46:
			UMCOMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 48:
			COMMENTERR_action((RuleContext)_localctx, actionIndex);
			break;
		case 49:
			NOMATCH_action((RuleContext)_localctx, actionIndex);
			break;
		case 50:
			ERR1_action((RuleContext)_localctx, actionIndex);
			break;
		case 51:
			ERR2_action((RuleContext)_localctx, actionIndex);
			break;
		case 55:
			ERR3_action((RuleContext)_localctx, actionIndex);
			break;
		case 57:
			ERR4_action((RuleContext)_localctx, actionIndex);
			break;
		case 58:
			ERR5_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void STR_CONST_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 processString();
			break;
		}
	}
	private void ERROR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 reportError("EOF in string constant"); 
			break;
		case 2:
			 reportError("Unescapted new line in string literal"); 
			break;
		case 3:
			 reportError("Unterminated string constant"); 
			break;
		}
	}
	private void END_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			 reportError("Unmatched *)"); 
			break;
		}
	}
	private void UMCOMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			 reportError("Unmatched *)"); 
			break;
		}
	}
	private void COMMENTERR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			 reportError("EOF in comment"); 
			break;
		}
	}
	private void NOMATCH_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 7:
			 noMatch(); 
			break;
		}
	}
	private void ERR1_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 8:
			 reportError("EOF in comment"); 
			break;
		}
	}
	private void ERR2_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 9:
			 reportError("EOF in comment"); 
			break;
		}
	}
	private void ERR3_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 10:
			 reportError("EOF in comment"); 
			break;
		}
	}
	private void ERR4_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 11:
			 reportError("EOF in comment"); 
			break;
		}
	}
	private void ERR5_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 12:
			 reportError("EOF in comment"); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2>\u01b6\b\1\b\1\b"+
		"\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n"+
		"\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21"+
		"\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30"+
		"\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37"+
		"\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t"+
		"*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63"+
		"\4\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t"+
		"<\4=\t=\4>\t>\3\2\3\2\3\2\7\2\u0083\n\2\f\2\16\2\u0086\13\2\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\4\3\4\7\4\u0090\n\4\f\4\16\4\u0093\13\4\3\4\3\4\3\4\3"+
		"\4\3\4\7\4\u009a\n\4\f\4\16\4\u009d\13\4\3\4\3\4\7\4\u00a1\n\4\f\4\16"+
		"\4\u00a4\13\4\3\4\3\4\3\4\3\4\7\4\u00aa\n\4\f\4\16\4\u00ad\13\4\3\4\3"+
		"\4\5\4\u00b1\n\4\3\5\3\5\3\6\3\6\3\6\3\7\5\7\u00b9\n\7\3\7\6\7\u00bc\n"+
		"\7\r\7\16\7\u00bd\3\b\3\b\7\b\u00c2\n\b\f\b\16\b\u00c5\13\b\3\t\3\t\7"+
		"\t\u00c9\n\t\f\t\16\t\u00cc\13\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3"+
		"\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24"+
		"\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u00fb\n\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36"+
		"\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3#\3#\3"+
		"#\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3"+
		"\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3,\3,\3,\3,"+
		"\3,\3-\6-\u0150\n-\r-\16-\u0151\3-\3-\3.\3.\3.\3.\7.\u015a\n.\f.\16.\u015d"+
		"\13.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\61\3\61"+
		"\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\64"+
		"\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\67\3\67\3\67\3\67\3\67\3\67\38\38\38\38\39\39\39\39\3:\3:\3:\3"+
		":\3:\3:\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\3=\3>\3>\3"+
		">\3>\4\u00a2\u015b\2?\5\b\7\2\t\3\13\r\r\31\17\7\21\4\23\5\25\32\27\33"+
		"\31\17\33\20\35\21\37\22!\t#\n%\13\'\27)\30+\23-\24/\f\61\16\63\25\65"+
		"\26\67\69\34;\35=+?\37A C*E\36G!I\"K#M&O\'Q$S,U(W)Y%[-]._/a\60c\61e\62"+
		"g\63i\64k\65m\66o\67q8s9u:w;y<{=}>\5\2\3\4\34\5\2\f\f$$^^\4\2\f\f$$\3"+
		"\2$$\6\2\f\f$$GHQQ\3\2\62;\3\2C\\\6\2\62;C\\aac|\3\2c|\4\2TTtt\4\2WWw"+
		"w\4\2GGgg\4\2CCcc\4\2NNnn\4\2UUuu\4\2EEee\4\2KKkk\4\2XXxx\4\2QQqq\4\2"+
		"FFff\4\2HHhh\4\2PPpp\4\2YYyy\4\2JJjj\4\2VVvv\4\2RRrr\7\2\13\f\16\17\""+
		"\"^^xx\u01c2\2\5\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"+
		"\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2"+
		"\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W"+
		"\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2"+
		"\2\2\2e\3\2\2\2\2g\3\2\2\2\3i\3\2\2\2\3k\3\2\2\2\3m\3\2\2\2\3o\3\2\2\2"+
		"\3q\3\2\2\2\4s\3\2\2\2\4u\3\2\2\2\4w\3\2\2\2\4y\3\2\2\2\4{\3\2\2\2\4}"+
		"\3\2\2\2\5\177\3\2\2\2\7\u008a\3\2\2\2\t\u00b0\3\2\2\2\13\u00b2\3\2\2"+
		"\2\r\u00b4\3\2\2\2\17\u00b8\3\2\2\2\21\u00bf\3\2\2\2\23\u00c6\3\2\2\2"+
		"\25\u00cd\3\2\2\2\27\u00d0\3\2\2\2\31\u00d3\3\2\2\2\33\u00d5\3\2\2\2\35"+
		"\u00d7\3\2\2\2\37\u00d9\3\2\2\2!\u00db\3\2\2\2#\u00dd\3\2\2\2%\u00df\3"+
		"\2\2\2\'\u00e1\3\2\2\2)\u00e3\3\2\2\2+\u00e5\3\2\2\2-\u00e7\3\2\2\2/\u00e9"+
		"\3\2\2\2\61\u00eb\3\2\2\2\63\u00ed\3\2\2\2\65\u00ef\3\2\2\2\67\u00fa\3"+
		"\2\2\29\u00fc\3\2\2\2;\u0102\3\2\2\2=\u0107\3\2\2\2?\u010e\3\2\2\2A\u0111"+
		"\3\2\2\2C\u0114\3\2\2\2E\u0118\3\2\2\2G\u011b\3\2\2\2I\u0124\3\2\2\2K"+
		"\u0128\3\2\2\2M\u012d\3\2\2\2O\u0133\3\2\2\2Q\u0138\3\2\2\2S\u013d\3\2"+
		"\2\2U\u0141\3\2\2\2W\u0146\3\2\2\2Y\u0149\3\2\2\2[\u014f\3\2\2\2]\u0155"+
		"\3\2\2\2_\u0162\3\2\2\2a\u0168\3\2\2\2c\u016d\3\2\2\2e\u0173\3\2\2\2g"+
		"\u0179\3\2\2\2i\u017c\3\2\2\2k\u0180\3\2\2\2m\u0186\3\2\2\2o\u018c\3\2"+
		"\2\2q\u0192\3\2\2\2s\u0196\3\2\2\2u\u019a\3\2\2\2w\u01a0\3\2\2\2y\u01a6"+
		"\3\2\2\2{\u01ac\3\2\2\2}\u01b2\3\2\2\2\177\u0084\7$\2\2\u0080\u0083\5"+
		"\7\3\2\u0081\u0083\n\2\2\2\u0082\u0080\3\2\2\2\u0082\u0081\3\2\2\2\u0083"+
		"\u0086\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2"+
		"\2\2\u0086\u0084\3\2\2\2\u0087\u0088\7$\2\2\u0088\u0089\b\2\2\2\u0089"+
		"\6\3\2\2\2\u008a\u008b\7^\2\2\u008b\u008c\13\2\2\2\u008c\b\3\2\2\2\u008d"+
		"\u0091\7$\2\2\u008e\u0090\n\3\2\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2"+
		"\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094\3\2\2\2\u0093"+
		"\u0091\3\2\2\2\u0094\u0095\7\2\2\3\u0095\u00b1\b\4\3\2\u0096\u009b\7$"+
		"\2\2\u0097\u009a\5\7\3\2\u0098\u009a\n\4\2\2\u0099\u0097\3\2\2\2\u0099"+
		"\u0098\3\2\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2"+
		"\2\2\u009c\u009e\3\2\2\2\u009d\u009b\3\2\2\2\u009e\u00a2\7\f\2\2\u009f"+
		"\u00a1\13\2\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2\u00a3\3"+
		"\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5"+
		"\u00a6\7$\2\2\u00a6\u00b1\b\4\4\2\u00a7\u00ab\7$\2\2\u00a8\u00aa\n\5\2"+
		"\2\u00a9\u00a8\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac"+
		"\3\2\2\2\u00ac\u00ae\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae\u00af\7\f\2\2\u00af"+
		"\u00b1\b\4\5\2\u00b0\u008d\3\2\2\2\u00b0\u0096\3\2\2\2\u00b0\u00a7\3\2"+
		"\2\2\u00b1\n\3\2\2\2\u00b2\u00b3\7=\2\2\u00b3\f\3\2\2\2\u00b4\u00b5\7"+
		"?\2\2\u00b5\u00b6\7@\2\2\u00b6\16\3\2\2\2\u00b7\u00b9\7/\2\2\u00b8\u00b7"+
		"\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00bc\t\6\2\2\u00bb"+
		"\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2"+
		"\2\2\u00be\20\3\2\2\2\u00bf\u00c3\t\7\2\2\u00c0\u00c2\t\b\2\2\u00c1\u00c0"+
		"\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4"+
		"\22\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00ca\t\t\2\2\u00c7\u00c9\t\b\2"+
		"\2\u00c8\u00c7\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb"+
		"\3\2\2\2\u00cb\24\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\7>\2\2\u00ce"+
		"\u00cf\7?\2\2\u00cf\26\3\2\2\2\u00d0\u00d1\7>\2\2\u00d1\u00d2\7/\2\2\u00d2"+
		"\30\3\2\2\2\u00d3\u00d4\7-\2\2\u00d4\32\3\2\2\2\u00d5\u00d6\7/\2\2\u00d6"+
		"\34\3\2\2\2\u00d7\u00d8\7,\2\2\u00d8\36\3\2\2\2\u00d9\u00da\7\61\2\2\u00da"+
		" \3\2\2\2\u00db\u00dc\7*\2\2\u00dc\"\3\2\2\2\u00dd\u00de\7+\2\2\u00de"+
		"$\3\2\2\2\u00df\u00e0\7<\2\2\u00e0&\3\2\2\2\u00e1\u00e2\7\177\2\2\u00e2"+
		"(\3\2\2\2\u00e3\u00e4\7\60\2\2\u00e4*\3\2\2\2\u00e5\u00e6\7\u0080\2\2"+
		"\u00e6,\3\2\2\2\u00e7\u00e8\7>\2\2\u00e8.\3\2\2\2\u00e9\u00ea\7B\2\2\u00ea"+
		"\60\3\2\2\2\u00eb\u00ec\7.\2\2\u00ec\62\3\2\2\2\u00ed\u00ee\7?\2\2\u00ee"+
		"\64\3\2\2\2\u00ef\u00f0\7}\2\2\u00f0\66\3\2\2\2\u00f1\u00f2\7v\2\2\u00f2"+
		"\u00f3\t\n\2\2\u00f3\u00f4\t\13\2\2\u00f4\u00fb\t\f\2\2\u00f5\u00f6\7"+
		"h\2\2\u00f6\u00f7\t\r\2\2\u00f7\u00f8\t\16\2\2\u00f8\u00f9\t\17\2\2\u00f9"+
		"\u00fb\t\f\2\2\u00fa\u00f1\3\2\2\2\u00fa\u00f5\3\2\2\2\u00fb8\3\2\2\2"+
		"\u00fc\u00fd\t\20\2\2\u00fd\u00fe\t\16\2\2\u00fe\u00ff\t\r\2\2\u00ff\u0100"+
		"\t\17\2\2\u0100\u0101\t\17\2\2\u0101:\3\2\2\2\u0102\u0103\t\f\2\2\u0103"+
		"\u0104\t\16\2\2\u0104\u0105\t\17\2\2\u0105\u0106\t\f\2\2\u0106<\3\2\2"+
		"\2\u0107\u0108\t\21\2\2\u0108\u0109\t\17\2\2\u0109\u010a\t\22\2\2\u010a"+
		"\u010b\t\23\2\2\u010b\u010c\t\21\2\2\u010c\u010d\t\24\2\2\u010d>\3\2\2"+
		"\2\u010e\u010f\t\21\2\2\u010f\u0110\t\25\2\2\u0110@\3\2\2\2\u0111\u0112"+
		"\t\21\2\2\u0112\u0113\t\26\2\2\u0113B\3\2\2\2\u0114\u0115\t\26\2\2\u0115"+
		"\u0116\t\f\2\2\u0116\u0117\t\27\2\2\u0117D\3\2\2\2\u0118\u0119\t\25\2"+
		"\2\u0119\u011a\t\21\2\2\u011aF\3\2\2\2\u011b\u011c\t\21\2\2\u011c\u011d"+
		"\t\26\2\2\u011d\u011e\t\30\2\2\u011e\u011f\t\f\2\2\u011f\u0120\t\n\2\2"+
		"\u0120\u0121\t\21\2\2\u0121\u0122\t\31\2\2\u0122\u0123\t\17\2\2\u0123"+
		"H\3\2\2\2\u0124\u0125\t\16\2\2\u0125\u0126\t\f\2\2\u0126\u0127\t\31\2"+
		"\2\u0127J\3\2\2\2\u0128\u0129\t\16\2\2\u0129\u012a\t\23\2\2\u012a\u012b"+
		"\t\23\2\2\u012b\u012c\t\32\2\2\u012cL\3\2\2\2\u012d\u012e\t\27\2\2\u012e"+
		"\u012f\t\30\2\2\u012f\u0130\t\21\2\2\u0130\u0131\t\16\2\2\u0131\u0132"+
		"\t\f\2\2\u0132N\3\2\2\2\u0133\u0134\t\20\2\2\u0134\u0135\t\r\2\2\u0135"+
		"\u0136\t\17\2\2\u0136\u0137\t\f\2\2\u0137P\3\2\2\2\u0138\u0139\t\32\2"+
		"\2\u0139\u013a\t\23\2\2\u013a\u013b\t\23\2\2\u013b\u013c\t\16\2\2\u013c"+
		"R\3\2\2\2\u013d\u013e\t\26\2\2\u013e\u013f\t\23\2\2\u013f\u0140\t\31\2"+
		"\2\u0140T\3\2\2\2\u0141\u0142\t\f\2\2\u0142\u0143\t\17\2\2\u0143\u0144"+
		"\t\r\2\2\u0144\u0145\t\20\2\2\u0145V\3\2\2\2\u0146\u0147\t\23\2\2\u0147"+
		"\u0148\t\25\2\2\u0148X\3\2\2\2\u0149\u014a\t\31\2\2\u014a\u014b\t\30\2"+
		"\2\u014b\u014c\t\f\2\2\u014c\u014d\t\26\2\2\u014dZ\3\2\2\2\u014e\u0150"+
		"\t\33\2\2\u014f\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u014f\3\2\2\2"+
		"\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154\b-\6\2\u0154\\\3"+
		"\2\2\2\u0155\u0156\7/\2\2\u0156\u0157\7/\2\2\u0157\u015b\3\2\2\2\u0158"+
		"\u015a\13\2\2\2\u0159\u0158\3\2\2\2\u015a\u015d\3\2\2\2\u015b\u015c\3"+
		"\2\2\2\u015b\u0159\3\2\2\2\u015c\u015e\3\2\2\2\u015d\u015b\3\2\2\2\u015e"+
		"\u015f\7\f\2\2\u015f\u0160\3\2\2\2\u0160\u0161\b.\6\2\u0161^\3\2\2\2\u0162"+
		"\u0163\7,\2\2\u0163\u0164\7+\2\2\u0164\u0165\3\2\2\2\u0165\u0166\7\2\2"+
		"\3\u0166\u0167\b/\7\2\u0167`\3\2\2\2\u0168\u0169\7,\2\2\u0169\u016a\7"+
		"+\2\2\u016a\u016b\3\2\2\2\u016b\u016c\b\60\b\2\u016cb\3\2\2\2\u016d\u016e"+
		"\7*\2\2\u016e\u016f\7,\2\2\u016f\u0170\3\2\2\2\u0170\u0171\b\61\t\2\u0171"+
		"\u0172\b\61\6\2\u0172d\3\2\2\2\u0173\u0174\7*\2\2\u0174\u0175\7,\2\2\u0175"+
		"\u0176\3\2\2\2\u0176\u0177\7\2\2\3\u0177\u0178\b\62\n\2\u0178f\3\2\2\2"+
		"\u0179\u017a\13\2\2\2\u017a\u017b\b\63\13\2\u017bh\3\2\2\2\u017c\u017d"+
		"\13\2\2\2\u017d\u017e\7\2\2\3\u017e\u017f\b\64\f\2\u017fj\3\2\2\2\u0180"+
		"\u0181\7*\2\2\u0181\u0182\7,\2\2\u0182\u0183\3\2\2\2\u0183\u0184\7\2\2"+
		"\3\u0184\u0185\b\65\r\2\u0185l\3\2\2\2\u0186\u0187\7*\2\2\u0187\u0188"+
		"\7,\2\2\u0188\u0189\3\2\2\2\u0189\u018a\b\66\16\2\u018a\u018b\b\66\6\2"+
		"\u018bn\3\2\2\2\u018c\u018d\7,\2\2\u018d\u018e\7+\2\2\u018e\u018f\3\2"+
		"\2\2\u018f\u0190\b\67\17\2\u0190\u0191\b\67\6\2\u0191p\3\2\2\2\u0192\u0193"+
		"\13\2\2\2\u0193\u0194\3\2\2\2\u0194\u0195\b8\6\2\u0195r\3\2\2\2\u0196"+
		"\u0197\13\2\2\2\u0197\u0198\7\2\2\3\u0198\u0199\b9\20\2\u0199t\3\2\2\2"+
		"\u019a\u019b\7*\2\2\u019b\u019c\7,\2\2\u019c\u019d\3\2\2\2\u019d\u019e"+
		"\b:\16\2\u019e\u019f\b:\6\2\u019fv\3\2\2\2\u01a0\u01a1\7,\2\2\u01a1\u01a2"+
		"\7+\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a4\7\2\2\3\u01a4\u01a5\b;\21\2\u01a5"+
		"x\3\2\2\2\u01a6\u01a7\7*\2\2\u01a7\u01a8\7,\2\2\u01a8\u01a9\3\2\2\2\u01a9"+
		"\u01aa\7\2\2\3\u01aa\u01ab\b<\22\2\u01abz\3\2\2\2\u01ac\u01ad\7,\2\2\u01ad"+
		"\u01ae\7+\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\b=\17\2\u01b0\u01b1\b=\6"+
		"\2\u01b1|\3\2\2\2\u01b2\u01b3\13\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5"+
		"\b>\6\2\u01b5~\3\2\2\2\24\2\3\4\u0082\u0084\u0091\u0099\u009b\u00a2\u00ab"+
		"\u00b0\u00b8\u00bd\u00c3\u00ca\u00fa\u0151\u015b\23\3\2\2\3\4\3\3\4\4"+
		"\3\4\5\b\2\2\3/\6\3\60\7\7\3\2\3\62\b\3\63\t\3\64\n\3\65\13\7\4\2\6\2"+
		"\2\39\f\3;\r\3<\16";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}