// Generated from C:/Users/Bazhenov Egor/IdeaProjects/MethodsTranslations/src/HW3\Calc.g4 by ANTLR 4.8
package com.antr;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CalcParser}.
 */
public interface CalcListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CalcParser#calc}.
	 * @param ctx the parse tree
	 */
	void enterCalc(CalcParser.CalcContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#calc}.
	 * @param ctx the parse tree
	 */
	void exitCalc(CalcParser.CalcContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalcParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CalcParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CalcParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalcParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(CalcParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(CalcParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalcParser#multExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultExpression(CalcParser.MultExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#multExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultExpression(CalcParser.MultExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalcParser#logExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogExpression(CalcParser.LogExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#logExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogExpression(CalcParser.LogExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalcParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(CalcParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalcParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(CalcParser.AtomContext ctx);
}