package tokens;

import visitors.TokenVisitor;

public class NumberToken extends Token {
    private final int value;

    public NumberToken(String s) {
        value = Integer.parseInt(s);
        type = TokenType.NUMBER;
    }

    public int getValue(){
        return value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("NUMBER(%d)", value);
    }
}
