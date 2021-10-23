package visitors;

import resources.ErrorMessage;
import tokens.Brace;
import tokens.NumberToken;
import tokens.Operation;
import tokens.Token;

import java.util.List;
import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private final Stack<Token> stack;
    private int ans;

    public CalcVisitor(List<Token> list) {
        stack = new Stack<>();
        stack.addAll(list);
        ans = 0;
    }

    public int calc() {
        if (!stack.isEmpty()) {
            Token t = stack.pop();
            t.accept(this);
        } else {
            System.err.println(ErrorMessage.EMPTY_LIST);
        }
        if (!stack.isEmpty()) {
            ans = 0;
            System.err.println(ErrorMessage.LONE_OPERAND);
        }
        return ans;
    }

    @Override
    public void visit(NumberToken token) {
        ans = token.getValue();
    }

    @Override
    public void visit(Brace token) {
        System.err.println(ErrorMessage.UNEXPECTED_BRACE);
    }

    @Override
    public void visit(Operation token) {
        if (!stack.isEmpty()) {
            stack.pop().accept(this);
            int first = ans;
            if (!stack.isEmpty()) {
                stack.pop().accept(this);
                int second = ans;
                ans = token.execute(first, second);
            } else {
                ans = 0;
                System.err.printf(ErrorMessage.NO_SECOND_OPERAND, token.toString());
            }
        } else {
            System.err.printf(ErrorMessage.NO_OPERANDS, token.toString());
        }
    }
}
