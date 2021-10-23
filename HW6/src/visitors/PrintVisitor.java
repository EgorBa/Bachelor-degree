package visitors;

import tokens.Brace;
import tokens.NumberToken;
import tokens.Operation;
import tokens.Token;

import java.util.List;

public class PrintVisitor implements TokenVisitor {
    private final StringBuilder str;
    private final String SPACE = " ";

    public PrintVisitor() {
        str = new StringBuilder();
    }

    public String print(List<Token> list) {
        for (Token t : list) {
            t.accept(this);
        }
        return str.toString();
    }

    @Override
    public void visit(NumberToken token) {
        str.append(token.toString()).append(SPACE);
    }

    @Override
    public void visit(Brace token) {
        str.append(token.toString()).append(SPACE);
    }

    @Override
    public void visit(Operation token) {
        str.append(token.toString()).append(SPACE);
    }
}
