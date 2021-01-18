// Generated from /Users/egor.bazhenov/Generator/src/grammerParser/GrammerParser.g4 by ANTLR 4.9
package grammerParser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GrammerParserParser}.
 */
public interface GrammerParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#grammarParser}.
	 * @param ctx the parse tree
	 */
	void enterGrammarParser(GrammerParserParser.GrammarParserContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#grammarParser}.
	 * @param ctx the parse tree
	 */
	void exitGrammarParser(GrammerParserParser.GrammarParserContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#parserName}.
	 * @param ctx the parse tree
	 */
	void enterParserName(GrammerParserParser.ParserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#parserName}.
	 * @param ctx the parse tree
	 */
	void exitParserName(GrammerParserParser.ParserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(GrammerParserParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(GrammerParserParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#globals}.
	 * @param ctx the parse tree
	 */
	void enterGlobals(GrammerParserParser.GlobalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#globals}.
	 * @param ctx the parse tree
	 */
	void exitGlobals(GrammerParserParser.GlobalsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobal(GrammerParserParser.GlobalContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobal(GrammerParserParser.GlobalContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#fields}.
	 * @param ctx the parse tree
	 */
	void enterFields(GrammerParserParser.FieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#fields}.
	 * @param ctx the parse tree
	 */
	void exitFields(GrammerParserParser.FieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(GrammerParserParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(GrammerParserParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#parserRules}.
	 * @param ctx the parse tree
	 */
	void enterParserRules(GrammerParserParser.ParserRulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#parserRules}.
	 * @param ctx the parse tree
	 */
	void exitParserRules(GrammerParserParser.ParserRulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#principle}.
	 * @param ctx the parse tree
	 */
	void enterPrinciple(GrammerParserParser.PrincipleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#principle}.
	 * @param ctx the parse tree
	 */
	void exitPrinciple(GrammerParserParser.PrincipleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#token}.
	 * @param ctx the parse tree
	 */
	void enterToken(GrammerParserParser.TokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#token}.
	 * @param ctx the parse tree
	 */
	void exitToken(GrammerParserParser.TokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#grammerRule}.
	 * @param ctx the parse tree
	 */
	void enterGrammerRule(GrammerParserParser.GrammerRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#grammerRule}.
	 * @param ctx the parse tree
	 */
	void exitGrammerRule(GrammerParserParser.GrammerRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(GrammerParserParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(GrammerParserParser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#contentRule}.
	 * @param ctx the parse tree
	 */
	void enterContentRule(GrammerParserParser.ContentRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#contentRule}.
	 * @param ctx the parse tree
	 */
	void exitContentRule(GrammerParserParser.ContentRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammerParserParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(GrammerParserParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammerParserParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(GrammerParserParser.NameContext ctx);
}