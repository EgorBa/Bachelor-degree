package states;

import tokens.Token;

import java.util.List;

public abstract class State {

    abstract public State next(List<Token> list, String str, int pos);

    public boolean isBrace(char chr) {
        return chr == ')' || chr == '(';
    }

    public boolean isDigit(char chr) {
        return Character.isDigit(chr);
    }

    public boolean isSkip(char chr) {
        return chr == ' ' || chr == '\n' || chr == '\t';
    }

    public boolean isOp(char chr) {
        return chr == '+' || chr == '-' || chr == '*' || chr == '/';
    }
}
