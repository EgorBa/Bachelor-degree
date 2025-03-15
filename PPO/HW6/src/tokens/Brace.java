package tokens;

import visitors.TokenVisitor;

public class Brace extends Token {

    public Brace(char chr) {
        switch (chr) {
            case '(' -> type = TokenType.LEFT;
            case ')' -> type = TokenType.RIGHT;
        }
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

}
