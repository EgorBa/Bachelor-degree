// Generated from /Users/egor.bazhenov/Generator/src/grammerParser/GrammerParser.g4 by ANTLR 4.9
package grammerParser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GrammerParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GrammerParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#grammarParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrammarParser(GrammerParserParser.GrammarParserContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#parserName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserName(GrammerParserParser.ParserNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(GrammerParserParser.HeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#globals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobals(GrammerParserParser.GlobalsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal(GrammerParserParser.GlobalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFields(GrammerParserParser.FieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(GrammerParserParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#parserRules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserRules(GrammerParserParser.ParserRulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#principle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrinciple(GrammerParserParser.PrincipleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#token}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToken(GrammerParserParser.TokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#grammerRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrammerRule(GrammerParserParser.GrammerRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#constructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor(GrammerParserParser.ConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#contentRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContentRule(GrammerParserParser.ContentRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammerParserParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(GrammerParserParser.NameContext ctx);
}