// Generated from D:/IDEAWorkSpace/SysYCompiler\SysY.g4 by ANTLR 4.8
package util.frontend;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SysYLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CONST=1, INT=2, FLOAT=3, VOID=4, IF=5, ELSE=6, WHILE=7, BREAK=8, CONTINUE=9, 
		RETURN=10, Identifier=11, IntConst=12, DECIMAL_CONST=13, OCTAL_CONST=14, 
		HEXADECIMAL_CONST=15, FloatConst=16, DigitSequence=17, FloatSuffix=18, 
		STRING=19, DOUBLE_QUOTATION=20, ADD=21, MINUS=22, MUL=23, DIV=24, MOD=25, 
		NOT=26, ASSIGN=27, LT=28, GT=29, LE=30, GE=31, EQ=32, NE=33, AND=34, OR=35, 
		LP=36, RP=37, LB=38, RB=39, LC=40, RC=41, COMMA=42, SEMICOLON=43, WS=44, 
		LineComment=45, BlockComment=46;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"CONST", "INT", "FLOAT", "VOID", "IF", "ELSE", "WHILE", "BREAK", "CONTINUE", 
			"RETURN", "Identifier", "IntConst", "DECIMAL_CONST", "OCTAL_CONST", "HEXADECIMAL_CONST", 
			"FloatConst", "FractionalConst", "ExponentPart", "Sign", "DigitSequence", 
			"FloatSuffix", "STRING", "ESC", "DOUBLE_QUOTATION", "ADD", "MINUS", "MUL", 
			"DIV", "MOD", "NOT", "ASSIGN", "LT", "GT", "LE", "GE", "EQ", "NE", "AND", 
			"OR", "LP", "RP", "LB", "RB", "LC", "RC", "COMMA", "SEMICOLON", "WS", 
			"LineComment", "BlockComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'const'", "'int'", "'float'", "'void'", "'if'", "'else'", "'while'", 
			"'break'", "'continue'", "'return'", null, null, null, null, null, null, 
			null, null, null, "'\"'", "'+'", "'-'", "'*'", "'/'", "'%'", "'!'", "'='", 
			"'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'||'", "'('", 
			"')'", "'['", "']'", "'{'", "'}'", "','", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "CONST", "INT", "FLOAT", "VOID", "IF", "ELSE", "WHILE", "BREAK", 
			"CONTINUE", "RETURN", "Identifier", "IntConst", "DECIMAL_CONST", "OCTAL_CONST", 
			"HEXADECIMAL_CONST", "FloatConst", "DigitSequence", "FloatSuffix", "STRING", 
			"DOUBLE_QUOTATION", "ADD", "MINUS", "MUL", "DIV", "MOD", "NOT", "ASSIGN", 
			"LT", "GT", "LE", "GE", "EQ", "NE", "AND", "OR", "LP", "RP", "LB", "RB", 
			"LC", "RC", "COMMA", "SEMICOLON", "WS", "LineComment", "BlockComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public SysYLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SysY.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\60\u0158\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\6\f\u00a4\n\f\r\f\16\f\u00a5\5\f\u00a8"+
		"\n\f\3\r\3\r\3\r\5\r\u00ad\n\r\3\16\3\16\3\16\6\16\u00b2\n\16\r\16\16"+
		"\16\u00b3\5\16\u00b6\n\16\3\17\3\17\3\17\6\17\u00bb\n\17\r\17\16\17\u00bc"+
		"\5\17\u00bf\n\17\3\20\3\20\3\20\3\20\5\20\u00c5\n\20\3\20\6\20\u00c8\n"+
		"\20\r\20\16\20\u00c9\3\21\3\21\5\21\u00ce\n\21\3\21\5\21\u00d1\n\21\3"+
		"\21\3\21\3\21\5\21\u00d6\n\21\5\21\u00d8\n\21\3\22\5\22\u00db\n\22\3\22"+
		"\3\22\3\22\3\22\3\22\5\22\u00e2\n\22\3\23\3\23\5\23\u00e6\n\23\3\23\3"+
		"\23\3\24\3\24\3\25\6\25\u00ed\n\25\r\25\16\25\u00ee\3\26\3\26\3\27\3\27"+
		"\3\27\7\27\u00f6\n\27\f\27\16\27\u00f9\13\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\5\30\u0101\n\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35"+
		"\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3"+
		"&\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3"+
		"/\3\60\3\60\3\61\6\61\u013a\n\61\r\61\16\61\u013b\3\61\3\61\3\62\3\62"+
		"\3\62\3\62\7\62\u0144\n\62\f\62\16\62\u0147\13\62\3\62\3\62\3\63\3\63"+
		"\3\63\3\63\7\63\u014f\n\63\f\63\16\63\u0152\13\63\3\63\3\63\3\63\3\63"+
		"\3\63\4\u00f7\u0150\2\64\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\2%\2\'\2)\23+\24-\25/\2\61\26\63\27"+
		"\65\30\67\319\32;\33=\34?\35A\36C\37E G!I\"K#M$O%Q&S\'U(W)Y*[+],_-a.c"+
		"/e\60\3\2\r\5\2C\\aac|\6\2\62;C\\aac|\3\2\63;\3\2\62;\3\2\629\5\2\62;"+
		"CHch\4\2GGgg\4\2--//\4\2HHhh\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u016b"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2)\3\2"+
		"\2\2\2+\3\2\2\2\2-\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67"+
		"\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2"+
		"\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2"+
		"\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]"+
		"\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\3g\3\2\2\2\5m\3\2"+
		"\2\2\7q\3\2\2\2\tw\3\2\2\2\13|\3\2\2\2\r\177\3\2\2\2\17\u0084\3\2\2\2"+
		"\21\u008a\3\2\2\2\23\u0090\3\2\2\2\25\u0099\3\2\2\2\27\u00a7\3\2\2\2\31"+
		"\u00ac\3\2\2\2\33\u00b5\3\2\2\2\35\u00be\3\2\2\2\37\u00c4\3\2\2\2!\u00d7"+
		"\3\2\2\2#\u00e1\3\2\2\2%\u00e3\3\2\2\2\'\u00e9\3\2\2\2)\u00ec\3\2\2\2"+
		"+\u00f0\3\2\2\2-\u00f2\3\2\2\2/\u0100\3\2\2\2\61\u0102\3\2\2\2\63\u0104"+
		"\3\2\2\2\65\u0106\3\2\2\2\67\u0108\3\2\2\29\u010a\3\2\2\2;\u010c\3\2\2"+
		"\2=\u010e\3\2\2\2?\u0110\3\2\2\2A\u0112\3\2\2\2C\u0114\3\2\2\2E\u0116"+
		"\3\2\2\2G\u0119\3\2\2\2I\u011c\3\2\2\2K\u011f\3\2\2\2M\u0122\3\2\2\2O"+
		"\u0125\3\2\2\2Q\u0128\3\2\2\2S\u012a\3\2\2\2U\u012c\3\2\2\2W\u012e\3\2"+
		"\2\2Y\u0130\3\2\2\2[\u0132\3\2\2\2]\u0134\3\2\2\2_\u0136\3\2\2\2a\u0139"+
		"\3\2\2\2c\u013f\3\2\2\2e\u014a\3\2\2\2gh\7e\2\2hi\7q\2\2ij\7p\2\2jk\7"+
		"u\2\2kl\7v\2\2l\4\3\2\2\2mn\7k\2\2no\7p\2\2op\7v\2\2p\6\3\2\2\2qr\7h\2"+
		"\2rs\7n\2\2st\7q\2\2tu\7c\2\2uv\7v\2\2v\b\3\2\2\2wx\7x\2\2xy\7q\2\2yz"+
		"\7k\2\2z{\7f\2\2{\n\3\2\2\2|}\7k\2\2}~\7h\2\2~\f\3\2\2\2\177\u0080\7g"+
		"\2\2\u0080\u0081\7n\2\2\u0081\u0082\7u\2\2\u0082\u0083\7g\2\2\u0083\16"+
		"\3\2\2\2\u0084\u0085\7y\2\2\u0085\u0086\7j\2\2\u0086\u0087\7k\2\2\u0087"+
		"\u0088\7n\2\2\u0088\u0089\7g\2\2\u0089\20\3\2\2\2\u008a\u008b\7d\2\2\u008b"+
		"\u008c\7t\2\2\u008c\u008d\7g\2\2\u008d\u008e\7c\2\2\u008e\u008f\7m\2\2"+
		"\u008f\22\3\2\2\2\u0090\u0091\7e\2\2\u0091\u0092\7q\2\2\u0092\u0093\7"+
		"p\2\2\u0093\u0094\7v\2\2\u0094\u0095\7k\2\2\u0095\u0096\7p\2\2\u0096\u0097"+
		"\7w\2\2\u0097\u0098\7g\2\2\u0098\24\3\2\2\2\u0099\u009a\7t\2\2\u009a\u009b"+
		"\7g\2\2\u009b\u009c\7v\2\2\u009c\u009d\7w\2\2\u009d\u009e\7t\2\2\u009e"+
		"\u009f\7p\2\2\u009f\26\3\2\2\2\u00a0\u00a8\t\2\2\2\u00a1\u00a3\t\2\2\2"+
		"\u00a2\u00a4\t\3\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a3"+
		"\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a0\3\2\2\2\u00a7"+
		"\u00a1\3\2\2\2\u00a8\30\3\2\2\2\u00a9\u00ad\5\33\16\2\u00aa\u00ad\5\35"+
		"\17\2\u00ab\u00ad\5\37\20\2\u00ac\u00a9\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac"+
		"\u00ab\3\2\2\2\u00ad\32\3\2\2\2\u00ae\u00b6\t\4\2\2\u00af\u00b1\t\4\2"+
		"\2\u00b0\u00b2\t\5\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b1"+
		"\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b6\3\2\2\2\u00b5\u00ae\3\2\2\2\u00b5"+
		"\u00af\3\2\2\2\u00b6\34\3\2\2\2\u00b7\u00bf\7\62\2\2\u00b8\u00ba\7\62"+
		"\2\2\u00b9\u00bb\t\6\2\2\u00ba\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc"+
		"\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bf\3\2\2\2\u00be\u00b7\3\2"+
		"\2\2\u00be\u00b8\3\2\2\2\u00bf\36\3\2\2\2\u00c0\u00c1\7\62\2\2\u00c1\u00c5"+
		"\7z\2\2\u00c2\u00c3\7\62\2\2\u00c3\u00c5\7Z\2\2\u00c4\u00c0\3\2\2\2\u00c4"+
		"\u00c2\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c8\t\7\2\2\u00c7\u00c6\3\2"+
		"\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca"+
		" \3\2\2\2\u00cb\u00cd\5#\22\2\u00cc\u00ce\5%\23\2\u00cd\u00cc\3\2\2\2"+
		"\u00cd\u00ce\3\2\2\2\u00ce\u00d0\3\2\2\2\u00cf\u00d1\5+\26\2\u00d0\u00cf"+
		"\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d8\3\2\2\2\u00d2\u00d3\5)\25\2\u00d3"+
		"\u00d5\5%\23\2\u00d4\u00d6\5+\26\2\u00d5\u00d4\3\2\2\2\u00d5\u00d6\3\2"+
		"\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00cb\3\2\2\2\u00d7\u00d2\3\2\2\2\u00d8"+
		"\"\3\2\2\2\u00d9\u00db\5)\25\2\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2"+
		"\u00db\u00dc\3\2\2\2\u00dc\u00dd\7\60\2\2\u00dd\u00e2\5)\25\2\u00de\u00df"+
		"\5)\25\2\u00df\u00e0\7\60\2\2\u00e0\u00e2\3\2\2\2\u00e1\u00da\3\2\2\2"+
		"\u00e1\u00de\3\2\2\2\u00e2$\3\2\2\2\u00e3\u00e5\t\b\2\2\u00e4\u00e6\5"+
		"\'\24\2\u00e5\u00e4\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"\u00e8\5)\25\2\u00e8&\3\2\2\2\u00e9\u00ea\t\t\2\2\u00ea(\3\2\2\2\u00eb"+
		"\u00ed\t\5\2\2\u00ec\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ec\3\2"+
		"\2\2\u00ee\u00ef\3\2\2\2\u00ef*\3\2\2\2\u00f0\u00f1\t\n\2\2\u00f1,\3\2"+
		"\2\2\u00f2\u00f7\5\61\31\2\u00f3\u00f6\5/\30\2\u00f4\u00f6\13\2\2\2\u00f5"+
		"\u00f3\3\2\2\2\u00f5\u00f4\3\2\2\2\u00f6\u00f9\3\2\2\2\u00f7\u00f8\3\2"+
		"\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00fa\3\2\2\2\u00f9\u00f7\3\2\2\2\u00fa"+
		"\u00fb\5\61\31\2\u00fb.\3\2\2\2\u00fc\u00fd\7^\2\2\u00fd\u0101\7$\2\2"+
		"\u00fe\u00ff\7^\2\2\u00ff\u0101\7^\2\2\u0100\u00fc\3\2\2\2\u0100\u00fe"+
		"\3\2\2\2\u0101\60\3\2\2\2\u0102\u0103\7$\2\2\u0103\62\3\2\2\2\u0104\u0105"+
		"\7-\2\2\u0105\64\3\2\2\2\u0106\u0107\7/\2\2\u0107\66\3\2\2\2\u0108\u0109"+
		"\7,\2\2\u01098\3\2\2\2\u010a\u010b\7\61\2\2\u010b:\3\2\2\2\u010c\u010d"+
		"\7\'\2\2\u010d<\3\2\2\2\u010e\u010f\7#\2\2\u010f>\3\2\2\2\u0110\u0111"+
		"\7?\2\2\u0111@\3\2\2\2\u0112\u0113\7>\2\2\u0113B\3\2\2\2\u0114\u0115\7"+
		"@\2\2\u0115D\3\2\2\2\u0116\u0117\7>\2\2\u0117\u0118\7?\2\2\u0118F\3\2"+
		"\2\2\u0119\u011a\7@\2\2\u011a\u011b\7?\2\2\u011bH\3\2\2\2\u011c\u011d"+
		"\7?\2\2\u011d\u011e\7?\2\2\u011eJ\3\2\2\2\u011f\u0120\7#\2\2\u0120\u0121"+
		"\7?\2\2\u0121L\3\2\2\2\u0122\u0123\7(\2\2\u0123\u0124\7(\2\2\u0124N\3"+
		"\2\2\2\u0125\u0126\7~\2\2\u0126\u0127\7~\2\2\u0127P\3\2\2\2\u0128\u0129"+
		"\7*\2\2\u0129R\3\2\2\2\u012a\u012b\7+\2\2\u012bT\3\2\2\2\u012c\u012d\7"+
		"]\2\2\u012dV\3\2\2\2\u012e\u012f\7_\2\2\u012fX\3\2\2\2\u0130\u0131\7}"+
		"\2\2\u0131Z\3\2\2\2\u0132\u0133\7\177\2\2\u0133\\\3\2\2\2\u0134\u0135"+
		"\7.\2\2\u0135^\3\2\2\2\u0136\u0137\7=\2\2\u0137`\3\2\2\2\u0138\u013a\t"+
		"\13\2\2\u0139\u0138\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u0139\3\2\2\2\u013b"+
		"\u013c\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\b\61\2\2\u013eb\3\2\2\2"+
		"\u013f\u0140\7\61\2\2\u0140\u0141\7\61\2\2\u0141\u0145\3\2\2\2\u0142\u0144"+
		"\n\f\2\2\u0143\u0142\3\2\2\2\u0144\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145"+
		"\u0146\3\2\2\2\u0146\u0148\3\2\2\2\u0147\u0145\3\2\2\2\u0148\u0149\b\62"+
		"\2\2\u0149d\3\2\2\2\u014a\u014b\7\61\2\2\u014b\u014c\7,\2\2\u014c\u0150"+
		"\3\2\2\2\u014d\u014f\13\2\2\2\u014e\u014d\3\2\2\2\u014f\u0152\3\2\2\2"+
		"\u0150\u0151\3\2\2\2\u0150\u014e\3\2\2\2\u0151\u0153\3\2\2\2\u0152\u0150"+
		"\3\2\2\2\u0153\u0154\7,\2\2\u0154\u0155\7\61\2\2\u0155\u0156\3\2\2\2\u0156"+
		"\u0157\b\63\2\2\u0157f\3\2\2\2\32\2\u00a5\u00a7\u00ac\u00b3\u00b5\u00bc"+
		"\u00be\u00c4\u00c9\u00cd\u00d0\u00d5\u00d7\u00da\u00e1\u00e5\u00ee\u00f5"+
		"\u00f7\u0100\u013b\u0145\u0150\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}