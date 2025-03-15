// Generated from /Users/egor.bazhenov/Generator/src/grammerParser/GrammerParser.g4 by ANTLR 4.9
package grammerParser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GrammerParserParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		GRAMMAR=1, IMPORT=2, HEADER=3, FIELDS=4, GLOBALS=5, PACKAGE=6, SKIP_RULE=7, 
		POINT=8, COMMA=9, OR=10, COLON=11, SEMICOLON=12, STAR=13, OPEN_BRACKET=14, 
		CLOSE_BRACKET=15, SQUARE_OPEN=16, SQUARE_CLOSE=17, EQUALS=18, FIGURE_OPEN=19, 
		FIGURE_CLOSE=20, MORE_RULE=21, LESS=22, TOKEN=23, NAME=24, TYPE=25, REGEX=26, 
		CODE=27, FUNC_ARGUMENT=28, WHITESPACE=29;
	public static final int
		RULE_grammarParser = 0, RULE_parserName = 1, RULE_header = 2, RULE_globals = 3, 
		RULE_global = 4, RULE_fields = 5, RULE_field = 6, RULE_parserRules = 7, 
		RULE_principle = 8, RULE_token = 9, RULE_grammerRule = 10, RULE_constructor = 11, 
		RULE_contentRule = 12, RULE_name = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"grammarParser", "parserName", "header", "globals", "global", "fields", 
			"field", "parserRules", "principle", "token", "grammerRule", "constructor", 
			"contentRule", "name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'grammar'", "'import'", "'header'", "'fields'", "'globals'", "'package'", 
			"'-> skip'", "'.'", "','", "'|'", "':'", "';'", "'*'", "'('", "')'", 
			"'['", "']'", "'='", "'{'", "'}'", "'>'", "'<'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "GRAMMAR", "IMPORT", "HEADER", "FIELDS", "GLOBALS", "PACKAGE", 
			"SKIP_RULE", "POINT", "COMMA", "OR", "COLON", "SEMICOLON", "STAR", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "SQUARE_OPEN", "SQUARE_CLOSE", "EQUALS", "FIGURE_OPEN", 
			"FIGURE_CLOSE", "MORE_RULE", "LESS", "TOKEN", "NAME", "TYPE", "REGEX", 
			"CODE", "FUNC_ARGUMENT", "WHITESPACE"
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

	@Override
	public String getGrammarFileName() { return "GrammerParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GrammerParserParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class GrammarParserContext extends ParserRuleContext {
		public ParserNameContext parserName() {
			return getRuleContext(ParserNameContext.class,0);
		}
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public ParserRulesContext parserRules() {
			return getRuleContext(ParserRulesContext.class,0);
		}
		public GlobalsContext globals() {
			return getRuleContext(GlobalsContext.class,0);
		}
		public FieldsContext fields() {
			return getRuleContext(FieldsContext.class,0);
		}
		public GrammarParserContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_grammarParser; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterGrammarParser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitGrammarParser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitGrammarParser(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GrammarParserContext grammarParser() throws RecognitionException {
		GrammarParserContext _localctx = new GrammarParserContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_grammarParser);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			parserName();
			setState(29);
			header();
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GLOBALS) {
				{
				setState(30);
				globals();
				}
			}

			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FIELDS) {
				{
				setState(33);
				fields();
				}
			}

			setState(36);
			parserRules();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParserNameContext extends ParserRuleContext {
		public TerminalNode GRAMMAR() { return getToken(GrammerParserParser.GRAMMAR, 0); }
		public TerminalNode NAME() { return getToken(GrammerParserParser.NAME, 0); }
		public ParserNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterParserName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitParserName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitParserName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserNameContext parserName() throws RecognitionException {
		ParserNameContext _localctx = new ParserNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_parserName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(GRAMMAR);
			setState(39);
			match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public TerminalNode HEADER() { return getToken(GrammerParserParser.HEADER, 0); }
		public TerminalNode CODE() { return getToken(GrammerParserParser.CODE, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitHeader(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(HEADER);
			setState(42);
			match(CODE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GlobalsContext extends ParserRuleContext {
		public TerminalNode GLOBALS() { return getToken(GrammerParserParser.GLOBALS, 0); }
		public TerminalNode SQUARE_OPEN() { return getToken(GrammerParserParser.SQUARE_OPEN, 0); }
		public TerminalNode SQUARE_CLOSE() { return getToken(GrammerParserParser.SQUARE_CLOSE, 0); }
		public List<GlobalContext> global() {
			return getRuleContexts(GlobalContext.class);
		}
		public GlobalContext global(int i) {
			return getRuleContext(GlobalContext.class,i);
		}
		public GlobalsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globals; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterGlobals(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitGlobals(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitGlobals(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalsContext globals() throws RecognitionException {
		GlobalsContext _localctx = new GlobalsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_globals);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			match(GLOBALS);
			setState(45);
			match(SQUARE_OPEN);
			setState(47); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(46);
				global();
				}
				}
				setState(49); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NAME );
			setState(51);
			match(SQUARE_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GlobalContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(GrammerParserParser.NAME, 0); }
		public TerminalNode EQUALS() { return getToken(GrammerParserParser.EQUALS, 0); }
		public TerminalNode TYPE() { return getToken(GrammerParserParser.TYPE, 0); }
		public TerminalNode SEMICOLON() { return getToken(GrammerParserParser.SEMICOLON, 0); }
		public GlobalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterGlobal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitGlobal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitGlobal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalContext global() throws RecognitionException {
		GlobalContext _localctx = new GlobalContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_global);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			match(NAME);
			setState(54);
			match(EQUALS);
			setState(55);
			match(TYPE);
			setState(56);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldsContext extends ParserRuleContext {
		public TerminalNode FIELDS() { return getToken(GrammerParserParser.FIELDS, 0); }
		public TerminalNode SQUARE_OPEN() { return getToken(GrammerParserParser.SQUARE_OPEN, 0); }
		public TerminalNode SQUARE_CLOSE() { return getToken(GrammerParserParser.SQUARE_CLOSE, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public FieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitFields(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitFields(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldsContext fields() throws RecognitionException {
		FieldsContext _localctx = new FieldsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_fields);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(FIELDS);
			setState(59);
			match(SQUARE_OPEN);
			setState(61); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(60);
				field();
				}
				}
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NAME );
			setState(65);
			match(SQUARE_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(GrammerParserParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(GrammerParserParser.NAME, i);
		}
		public TerminalNode EQUALS() { return getToken(GrammerParserParser.EQUALS, 0); }
		public TerminalNode SEMICOLON() { return getToken(GrammerParserParser.SEMICOLON, 0); }
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_field);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			match(NAME);
			setState(68);
			match(EQUALS);
			setState(69);
			match(NAME);
			setState(70);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParserRulesContext extends ParserRuleContext {
		public List<PrincipleContext> principle() {
			return getRuleContexts(PrincipleContext.class);
		}
		public PrincipleContext principle(int i) {
			return getRuleContext(PrincipleContext.class,i);
		}
		public ParserRulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parserRules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterParserRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitParserRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitParserRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParserRulesContext parserRules() throws RecognitionException {
		ParserRulesContext _localctx = new ParserRulesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parserRules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(72);
				principle();
				}
				}
				setState(75); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==TOKEN || _la==NAME );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrincipleContext extends ParserRuleContext {
		public TokenContext token() {
			return getRuleContext(TokenContext.class,0);
		}
		public GrammerRuleContext grammerRule() {
			return getRuleContext(GrammerRuleContext.class,0);
		}
		public PrincipleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_principle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterPrinciple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitPrinciple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitPrinciple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrincipleContext principle() throws RecognitionException {
		PrincipleContext _localctx = new PrincipleContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_principle);
		try {
			setState(79);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TOKEN:
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				token();
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				grammerRule();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TokenContext extends ParserRuleContext {
		public TerminalNode TOKEN() { return getToken(GrammerParserParser.TOKEN, 0); }
		public TerminalNode COLON() { return getToken(GrammerParserParser.COLON, 0); }
		public TerminalNode REGEX() { return getToken(GrammerParserParser.REGEX, 0); }
		public TerminalNode SEMICOLON() { return getToken(GrammerParserParser.SEMICOLON, 0); }
		public TerminalNode CODE() { return getToken(GrammerParserParser.CODE, 0); }
		public TerminalNode SKIP_RULE() { return getToken(GrammerParserParser.SKIP_RULE, 0); }
		public TokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_token; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TokenContext token() throws RecognitionException {
		TokenContext _localctx = new TokenContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_token);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(TOKEN);
			setState(82);
			match(COLON);
			setState(83);
			match(REGEX);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CODE) {
				{
				setState(84);
				match(CODE);
				}
			}

			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SKIP_RULE) {
				{
				setState(87);
				match(SKIP_RULE);
				}
			}

			setState(90);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GrammerRuleContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(GrammerParserParser.NAME, 0); }
		public TerminalNode COLON() { return getToken(GrammerParserParser.COLON, 0); }
		public ContentRuleContext contentRule() {
			return getRuleContext(ContentRuleContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(GrammerParserParser.SEMICOLON, 0); }
		public ConstructorContext constructor() {
			return getRuleContext(ConstructorContext.class,0);
		}
		public GrammerRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_grammerRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterGrammerRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitGrammerRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitGrammerRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GrammerRuleContext grammerRule() throws RecognitionException {
		GrammerRuleContext _localctx = new GrammerRuleContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_grammerRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(NAME);
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SQUARE_OPEN) {
				{
				setState(93);
				constructor();
				}
			}

			setState(96);
			match(COLON);
			setState(97);
			contentRule(0);
			setState(98);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorContext extends ParserRuleContext {
		public TerminalNode SQUARE_OPEN() { return getToken(GrammerParserParser.SQUARE_OPEN, 0); }
		public List<TerminalNode> NAME() { return getTokens(GrammerParserParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(GrammerParserParser.NAME, i);
		}
		public List<TerminalNode> COLON() { return getTokens(GrammerParserParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(GrammerParserParser.COLON, i);
		}
		public TerminalNode SQUARE_CLOSE() { return getToken(GrammerParserParser.SQUARE_CLOSE, 0); }
		public TerminalNode SEMICOLON() { return getToken(GrammerParserParser.SEMICOLON, 0); }
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(SQUARE_OPEN);
			setState(101);
			match(NAME);
			setState(102);
			match(COLON);
			setState(103);
			match(NAME);
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMICOLON) {
				{
				setState(104);
				match(SEMICOLON);
				setState(105);
				match(NAME);
				setState(106);
				match(COLON);
				setState(107);
				match(NAME);
				}
			}

			setState(110);
			match(SQUARE_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContentRuleContext extends ParserRuleContext {
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public TerminalNode CODE() { return getToken(GrammerParserParser.CODE, 0); }
		public List<ContentRuleContext> contentRule() {
			return getRuleContexts(ContentRuleContext.class);
		}
		public ContentRuleContext contentRule(int i) {
			return getRuleContext(ContentRuleContext.class,i);
		}
		public TerminalNode OR() { return getToken(GrammerParserParser.OR, 0); }
		public ContentRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contentRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterContentRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitContentRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitContentRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentRuleContext contentRule() throws RecognitionException {
		return contentRule(0);
	}

	private ContentRuleContext contentRule(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ContentRuleContext _localctx = new ContentRuleContext(_ctx, _parentState);
		ContentRuleContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_contentRule, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(114); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(113);
					name();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(116); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(118);
				match(CODE);
				}
				break;
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(126);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ContentRuleContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_contentRule);
					setState(121);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(122);
					match(OR);
					setState(123);
					contentRule(2);
					}
					} 
				}
				setState(128);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public TerminalNode TOKEN() { return getToken(GrammerParserParser.TOKEN, 0); }
		public TerminalNode NAME() { return getToken(GrammerParserParser.NAME, 0); }
		public List<TerminalNode> FUNC_ARGUMENT() { return getTokens(GrammerParserParser.FUNC_ARGUMENT); }
		public TerminalNode FUNC_ARGUMENT(int i) {
			return getToken(GrammerParserParser.FUNC_ARGUMENT, i);
		}
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammerParserListener ) ((GrammerParserListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammerParserVisitor ) return ((GrammerParserVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_name);
		try {
			int _alt;
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TOKEN:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				match(TOKEN);
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				match(NAME);
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(131);
						match(FUNC_ARGUMENT);
						}
						} 
					}
					setState(136);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return contentRule_sempred((ContentRuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean contentRule_sempred(ContentRuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37\u008e\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\5\2\"\n\2\3\2\5"+
		"\2%\n\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\6\5\62\n\5\r\5\16"+
		"\5\63\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\6\7@\n\7\r\7\16\7A\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\6\tL\n\t\r\t\16\tM\3\n\3\n\5\nR\n\n\3\13\3"+
		"\13\3\13\3\13\5\13X\n\13\3\13\5\13[\n\13\3\13\3\13\3\f\3\f\5\fa\n\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\ro\n\r\3\r\3\r\3\16\3"+
		"\16\6\16u\n\16\r\16\16\16v\3\16\5\16z\n\16\3\16\3\16\3\16\7\16\177\n\16"+
		"\f\16\16\16\u0082\13\16\3\17\3\17\3\17\7\17\u0087\n\17\f\17\16\17\u008a"+
		"\13\17\5\17\u008c\n\17\3\17\2\3\32\20\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\2\2\2\u008e\2\36\3\2\2\2\4(\3\2\2\2\6+\3\2\2\2\b.\3\2\2\2\n\67\3\2"+
		"\2\2\f<\3\2\2\2\16E\3\2\2\2\20K\3\2\2\2\22Q\3\2\2\2\24S\3\2\2\2\26^\3"+
		"\2\2\2\30f\3\2\2\2\32r\3\2\2\2\34\u008b\3\2\2\2\36\37\5\4\3\2\37!\5\6"+
		"\4\2 \"\5\b\5\2! \3\2\2\2!\"\3\2\2\2\"$\3\2\2\2#%\5\f\7\2$#\3\2\2\2$%"+
		"\3\2\2\2%&\3\2\2\2&\'\5\20\t\2\'\3\3\2\2\2()\7\3\2\2)*\7\32\2\2*\5\3\2"+
		"\2\2+,\7\5\2\2,-\7\35\2\2-\7\3\2\2\2./\7\7\2\2/\61\7\22\2\2\60\62\5\n"+
		"\6\2\61\60\3\2\2\2\62\63\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\65\3\2"+
		"\2\2\65\66\7\23\2\2\66\t\3\2\2\2\678\7\32\2\289\7\24\2\29:\7\33\2\2:;"+
		"\7\16\2\2;\13\3\2\2\2<=\7\6\2\2=?\7\22\2\2>@\5\16\b\2?>\3\2\2\2@A\3\2"+
		"\2\2A?\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\7\23\2\2D\r\3\2\2\2EF\7\32\2\2FG"+
		"\7\24\2\2GH\7\32\2\2HI\7\16\2\2I\17\3\2\2\2JL\5\22\n\2KJ\3\2\2\2LM\3\2"+
		"\2\2MK\3\2\2\2MN\3\2\2\2N\21\3\2\2\2OR\5\24\13\2PR\5\26\f\2QO\3\2\2\2"+
		"QP\3\2\2\2R\23\3\2\2\2ST\7\31\2\2TU\7\r\2\2UW\7\34\2\2VX\7\35\2\2WV\3"+
		"\2\2\2WX\3\2\2\2XZ\3\2\2\2Y[\7\t\2\2ZY\3\2\2\2Z[\3\2\2\2[\\\3\2\2\2\\"+
		"]\7\16\2\2]\25\3\2\2\2^`\7\32\2\2_a\5\30\r\2`_\3\2\2\2`a\3\2\2\2ab\3\2"+
		"\2\2bc\7\r\2\2cd\5\32\16\2de\7\16\2\2e\27\3\2\2\2fg\7\22\2\2gh\7\32\2"+
		"\2hi\7\r\2\2in\7\32\2\2jk\7\16\2\2kl\7\32\2\2lm\7\r\2\2mo\7\32\2\2nj\3"+
		"\2\2\2no\3\2\2\2op\3\2\2\2pq\7\23\2\2q\31\3\2\2\2rt\b\16\1\2su\5\34\17"+
		"\2ts\3\2\2\2uv\3\2\2\2vt\3\2\2\2vw\3\2\2\2wy\3\2\2\2xz\7\35\2\2yx\3\2"+
		"\2\2yz\3\2\2\2z\u0080\3\2\2\2{|\f\3\2\2|}\7\f\2\2}\177\5\32\16\4~{\3\2"+
		"\2\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\33\3\2"+
		"\2\2\u0082\u0080\3\2\2\2\u0083\u008c\7\31\2\2\u0084\u0088\7\32\2\2\u0085"+
		"\u0087\7\36\2\2\u0086\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3"+
		"\2\2\2\u0088\u0089\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008b"+
		"\u0083\3\2\2\2\u008b\u0084\3\2\2\2\u008c\35\3\2\2\2\21!$\63AMQWZ`nvy\u0080"+
		"\u0088\u008b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}