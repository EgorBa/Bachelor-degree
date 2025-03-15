// Generated from C:/Users/Bazhenov Egor/IdeaProjects/MethodsTranslations/src/HW3\Calc.g4 by ANTLR 4.8
package com.antr;

import java.util.HashMap;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalcParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		ID=10, INT=11, WS=12;
	public static final int
		RULE_calc = 0, RULE_statement = 1, RULE_expr = 2, RULE_multExpression = 3, 
		RULE_logExpression = 4, RULE_atom = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"calc", "statement", "expr", "multExpression", "logExpression", "atom"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "';'", "'+'", "'-'", "'*'", "'/'", "'//'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "ID", "INT", 
			"WS"
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
	public String getGrammarFileName() { return "Calc.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	HashMap<String, Integer> map = new HashMap<>();

	public CalcParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class CalcContext extends ParserRuleContext {
		public String value;
		public StatementContext statement;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public CalcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_calc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterCalc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitCalc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitCalc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CalcContext calc() throws RecognitionException {
		CalcContext _localctx = new CalcContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_calc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(12);
				((CalcContext)_localctx).statement = statement();
				}
				}
				setState(15); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			((CalcContext)_localctx).value = ((CalcContext)_localctx).statement.value;
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

	public static class StatementContext extends ParserRuleContext {
		public String value;
		public Token ID;
		public ExprContext expr;
		public TerminalNode ID() { return getToken(CalcParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			((StatementContext)_localctx).ID = match(ID);
			setState(20);
			match(T__0);
			setState(21);
			((StatementContext)_localctx).expr = expr();
			setState(22);
			match(T__1);

				map.put((((StatementContext)_localctx).ID!=null?((StatementContext)_localctx).ID.getText():null),((StatementContext)_localctx).expr.value);
				System.out.println((((StatementContext)_localctx).ID!=null?((StatementContext)_localctx).ID.getText():null)+" = "+((StatementContext)_localctx).expr.value);
				
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

	public static class ExprContext extends ParserRuleContext {
		public Integer value;
		public MultExpressionContext me1;
		public MultExpressionContext me2;
		public List<MultExpressionContext> multExpression() {
			return getRuleContexts(MultExpressionContext.class);
		}
		public MultExpressionContext multExpression(int i) {
			return getRuleContext(MultExpressionContext.class,i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			((ExprContext)_localctx).me1 = multExpression();
			((ExprContext)_localctx).value =  ((ExprContext)_localctx).me1.value;
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2 || _la==T__3) {
				{
				setState(35);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__2:
					{
					setState(27);
					match(T__2);
					setState(28);
					((ExprContext)_localctx).me2 = multExpression();
					_localctx.value += ((ExprContext)_localctx).me2.value;
					}
					break;
				case T__3:
					{
					setState(31);
					match(T__3);
					setState(32);
					((ExprContext)_localctx).me2 = multExpression();
					_localctx.value -= ((ExprContext)_localctx).me2.value;
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class MultExpressionContext extends ParserRuleContext {
		public Integer value;
		public LogExpressionContext a1;
		public LogExpressionContext a2;
		public List<LogExpressionContext> logExpression() {
			return getRuleContexts(LogExpressionContext.class);
		}
		public LogExpressionContext logExpression(int i) {
			return getRuleContext(LogExpressionContext.class,i);
		}
		public MultExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterMultExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitMultExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitMultExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultExpressionContext multExpression() throws RecognitionException {
		MultExpressionContext _localctx = new MultExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_multExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			((MultExpressionContext)_localctx).a1 = logExpression();
			((MultExpressionContext)_localctx).value =  ((MultExpressionContext)_localctx).a1.value;
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4 || _la==T__5) {
				{
				setState(50);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(42);
					match(T__4);
					setState(43);
					((MultExpressionContext)_localctx).a2 = logExpression();
					_localctx.value *= ((MultExpressionContext)_localctx).a2.value;
					}
					break;
				case T__5:
					{
					setState(46);
					match(T__5);
					setState(47);
					((MultExpressionContext)_localctx).a2 = logExpression();
					_localctx.value /= ((MultExpressionContext)_localctx).a2.value;
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class LogExpressionContext extends ParserRuleContext {
		public Integer value;
		public AtomContext a1;
		public LogExpressionContext a2;
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public List<LogExpressionContext> logExpression() {
			return getRuleContexts(LogExpressionContext.class);
		}
		public LogExpressionContext logExpression(int i) {
			return getRuleContext(LogExpressionContext.class,i);
		}
		public LogExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterLogExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitLogExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitLogExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogExpressionContext logExpression() throws RecognitionException {
		LogExpressionContext _localctx = new LogExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_logExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			((LogExpressionContext)_localctx).a1 = atom();
			((LogExpressionContext)_localctx).value =  ((LogExpressionContext)_localctx).a1.value;
			setState(63);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(57);
					match(T__6);
					setState(58);
					((LogExpressionContext)_localctx).a2 = logExpression();
					((LogExpressionContext)_localctx).value =  (int) (Math.log(_localctx.value)/Math.log(((LogExpressionContext)_localctx).a2.value));
					}
					} 
				}
				setState(65);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
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

	public static class AtomContext extends ParserRuleContext {
		public Integer value;
		public Token ID;
		public Token INT;
		public ExprContext expr;
		public TerminalNode ID() { return getToken(CalcParser.ID, 0); }
		public TerminalNode INT() { return getToken(CalcParser.INT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalcListener ) ((CalcListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalcVisitor ) return ((CalcVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atom);
		int _la;
		try {
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(66);
					match(T__2);
					}
				}

				setState(69);
				((AtomContext)_localctx).ID = match(ID);
				((AtomContext)_localctx).value =  map.get((((AtomContext)_localctx).ID!=null?((AtomContext)_localctx).ID.getText():null));
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				match(T__3);
				setState(72);
				((AtomContext)_localctx).ID = match(ID);
				((AtomContext)_localctx).value =  -map.get((((AtomContext)_localctx).ID!=null?((AtomContext)_localctx).ID.getText():null));
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(74);
					match(T__2);
					}
				}

				setState(77);
				((AtomContext)_localctx).INT = match(INT);
				((AtomContext)_localctx).value =  Integer.parseInt((((AtomContext)_localctx).INT!=null?((AtomContext)_localctx).INT.getText():null));
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(79);
				match(T__3);
				setState(80);
				((AtomContext)_localctx).INT = match(INT);
				((AtomContext)_localctx).value =  -Integer.parseInt((((AtomContext)_localctx).INT!=null?((AtomContext)_localctx).INT.getText():null));
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(82);
				match(T__7);
				setState(83);
				((AtomContext)_localctx).expr = expr();
				setState(84);
				match(T__8);
				((AtomContext)_localctx).value =  ((AtomContext)_localctx).expr.value;
				}
				break;
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\16\\\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\6\2\20\n\2\r\2\16\2\21\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4"+
		"&\n\4\f\4\16\4)\13\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5\65\n"+
		"\5\f\5\16\58\13\5\3\6\3\6\3\6\3\6\3\6\3\6\7\6@\n\6\f\6\16\6C\13\6\3\7"+
		"\5\7F\n\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7N\n\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\5\7Z\n\7\3\7\2\2\b\2\4\6\b\n\f\2\2\2a\2\17\3\2\2\2\4\25\3"+
		"\2\2\2\6\33\3\2\2\2\b*\3\2\2\2\n9\3\2\2\2\fY\3\2\2\2\16\20\5\4\3\2\17"+
		"\16\3\2\2\2\20\21\3\2\2\2\21\17\3\2\2\2\21\22\3\2\2\2\22\23\3\2\2\2\23"+
		"\24\b\2\1\2\24\3\3\2\2\2\25\26\7\f\2\2\26\27\7\3\2\2\27\30\5\6\4\2\30"+
		"\31\7\4\2\2\31\32\b\3\1\2\32\5\3\2\2\2\33\34\5\b\5\2\34\'\b\4\1\2\35\36"+
		"\7\5\2\2\36\37\5\b\5\2\37 \b\4\1\2 &\3\2\2\2!\"\7\6\2\2\"#\5\b\5\2#$\b"+
		"\4\1\2$&\3\2\2\2%\35\3\2\2\2%!\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2"+
		"(\7\3\2\2\2)\'\3\2\2\2*+\5\n\6\2+\66\b\5\1\2,-\7\7\2\2-.\5\n\6\2./\b\5"+
		"\1\2/\65\3\2\2\2\60\61\7\b\2\2\61\62\5\n\6\2\62\63\b\5\1\2\63\65\3\2\2"+
		"\2\64,\3\2\2\2\64\60\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67"+
		"\t\3\2\2\28\66\3\2\2\29:\5\f\7\2:A\b\6\1\2;<\7\t\2\2<=\5\n\6\2=>\b\6\1"+
		"\2>@\3\2\2\2?;\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\13\3\2\2\2CA\3\2"+
		"\2\2DF\7\5\2\2ED\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\7\f\2\2HZ\b\7\1\2IJ\7\6"+
		"\2\2JK\7\f\2\2KZ\b\7\1\2LN\7\5\2\2ML\3\2\2\2MN\3\2\2\2NO\3\2\2\2OP\7\r"+
		"\2\2PZ\b\7\1\2QR\7\6\2\2RS\7\r\2\2SZ\b\7\1\2TU\7\n\2\2UV\5\6\4\2VW\7\13"+
		"\2\2WX\b\7\1\2XZ\3\2\2\2YE\3\2\2\2YI\3\2\2\2YM\3\2\2\2YQ\3\2\2\2YT\3\2"+
		"\2\2Z\r\3\2\2\2\13\21%\'\64\66AEMY";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}