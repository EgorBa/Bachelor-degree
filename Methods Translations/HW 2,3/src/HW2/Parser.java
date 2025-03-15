package HW2;

import java.text.ParseException;

public class Parser {
    LexicalAnalyzer lex;

    Tree A() throws ParseException {
        Tree sub;
        Tree sub1;
        switch (lex.curToken()) {
            case LPAREN:
            case CHAR:
// B
                sub = B();
// O
                sub1 = O();
                return new Tree("A", sub, sub1);
            case RPAREN:
            case END:
// eps
                return new Tree("A", new Tree("ε"));
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree O() throws ParseException {
        Tree sub;
        Tree sub1;
        switch (lex.curToken()) {
            case LPAREN:
            case CHAR:
// B
                sub = B();
// O
                sub1 = O();
                return new Tree("O", sub, sub1);
            case END:
            case RPAREN:
// eps
                return new Tree("O", new Tree("ε"));
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree B() throws ParseException {
        Tree sub;
        Tree sub1;
        switch (lex.curToken()) {
            case LPAREN:
            case CHAR:
// C
                sub = C();
// P
                sub1 = P();
                return new Tree("B", sub, sub1);
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree P() throws ParseException {
        Tree sub;
        Tree sub1;
        switch (lex.curToken()) {
            case CHOOSE:
// |
                lex.nextToken();
// C
                sub = C();
// P
                sub1 = P();
                return new Tree("P", new Tree("|"), sub, sub1);
            case LPAREN:
            case RPAREN:
            case CHAR:
            case END:
// eps
                return new Tree("P", new Tree("ε"));
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree C() throws ParseException {
        Tree sub;
        Tree sub1;
        switch (lex.curToken()) {
            case LPAREN:
            case CHAR:
// T
                sub = T();
// K
                sub1 = K();
                return new Tree("C", sub, sub1);
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree K() throws ParseException {
        Tree sub;
        switch (lex.curToken()) {
            case KLEENE:
// *
                lex.nextToken();
// K
                sub = K();
                return new Tree("K", new Tree("*"), sub);
            case LPAREN:
            case RPAREN:
            case CHAR:
            case CHOOSE:
            case END:
// eps
                return new Tree("K", new Tree("ε"));
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree T() throws ParseException {
        Tree sub;
        switch (lex.curToken()) {
            case LPAREN:
// (
                lex.nextToken();
// A
                sub = A();
// )
                if (lex.curToken() != Token.RPAREN) {
                    throw new ParseException(" ) expected at position " + lex.curPos(), 0);
                }
                lex.nextToken();
                return new Tree("T", new Tree("("), sub,
                        new Tree(")"));
            case CHAR:
// t
                char curChar = lex.prevChar();
                lex.nextToken();
                return new Tree("T", new Tree(String.valueOf(curChar)));
            default:
                throw new ParseException(" unexpected terminal at position " + lex.curPos(), 0);
        }
    }

    Tree parse(String is) throws ParseException {
        lex = new LexicalAnalyzer(is);
        lex.nextToken();
        return A();
    }
}