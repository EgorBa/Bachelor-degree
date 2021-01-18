// Generated from C:/Users/Bazhenov Egor/IdeaProjects/MethodsTranslations/src/HW3\Calc.g4 by ANTLR 4.8
package com.antr;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CalcParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CalcVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CalcParser#calc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalc(CalcParser.CalcContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalcParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CalcParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalcParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(CalcParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalcParser#multExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExpression(CalcParser.MultExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalcParser#logExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogExpression(CalcParser.LogExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalcParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(CalcParser.AtomContext ctx);
}