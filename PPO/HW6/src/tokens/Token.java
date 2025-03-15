package tokens;

import visitors.TokenVisitor;

abstract public class Token {
    protected TokenType type;

    public TokenType getType() {
        return type;
    }

    public boolean isOp() {
        return isPlusOrMinus() || isMulOrDiv();
    }

    public boolean isBrace() {
        return type == TokenType.RIGHT || type == TokenType.LEFT;
    }

    public boolean isPlusOrMinus() {
        return type == TokenType.PLUS || type == TokenType.MINUS;
    }

    public boolean isMulOrDiv() {
        return type == TokenType.DIV || type == TokenType.MUL;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    abstract public void accept(TokenVisitor visitor);
}