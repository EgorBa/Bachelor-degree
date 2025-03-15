package visitors;

import tokens.Brace;
import tokens.NumberToken;
import tokens.Operation;

public interface TokenVisitor {
    void visit(NumberToken token);

    void visit(Brace token);

    void visit(Operation token);
}