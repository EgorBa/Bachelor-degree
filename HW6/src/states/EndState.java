package states;

import tokens.Token;

import java.util.List;

public class EndState extends State {
    @Override
    public State next(List<Token> list, String str, int pos) {
        return this;
    }
}
