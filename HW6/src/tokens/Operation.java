package tokens;

import visitors.TokenVisitor;

public class Operation extends Token {

    public Operation(char chr) {
        switch (chr) {
            case '+' -> type = TokenType.PLUS;
            case '-' -> type = TokenType.MINUS;
            case '*' -> type = TokenType.MUL;
            case '/' -> type = TokenType.DIV;
        }
    }

    public int execute(int a, int b) {
        int result = 0;
        switch (type) {
            case PLUS -> result = b + a;
            case MINUS -> result = b - a;
            case MUL -> result = b * a;
            case DIV -> result = b / a;
        }
        return result;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
