package visitors;

import resources.ErrorMessage;
import tokens.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ParseVisitor implements TokenVisitor {
    Stack<Token> stack;
    LinkedList<Token> queue;

    public ParseVisitor() {
        stack = new Stack<>();
        queue = new LinkedList<>();
    }

    public List<Token> toRPN(List<Token> list) {
        for (Token t : list) {
            t.accept(this);
        }
        while (!stack.isEmpty()) {
            Token t = stack.pop();
            if (t.isBrace()) {
                System.err.println(ErrorMessage.LEFT_BRACE_ERROR);
            } else {
                queue.add(t);
            }
        }
        return queue;
    }

    @Override
    public void visit(NumberToken token) {
        queue.addLast(token);
    }

    @Override
    public void visit(Brace token) {
        if (token.getType() == TokenType.LEFT) {
            stack.add(token);
        } else if (token.getType() == TokenType.RIGHT) {
            Token t = null;
            while (!stack.isEmpty()) {
                t = stack.pop();
                if (t.getType() == TokenType.LEFT) {
                    break;
                } else {
                    queue.addLast(t);
                }
            }
            if (stack.isEmpty() && (t == null || t.getType() != TokenType.LEFT)) {
                System.err.println(ErrorMessage.RIGHT_BRACE_ERROR);
            }
        }
    }

    @Override
    public void visit(Operation token) {
        if (token.isPlusOrMinus()) {
            while (!stack.isEmpty()) {
                Token t = stack.pop();
                if (t.isOp()) {
                    queue.add(t);
                } else {
                    stack.add(t);
                    break;
                }
            }
        } else if (token.isMulOrDiv()) {
            while (!stack.isEmpty()) {
                Token t = stack.pop();
                if (t.isMulOrDiv()) {
                    queue.add(t);
                } else {
                    stack.add(t);
                    break;
                }
            }
        }
        stack.add(token);
    }
}
