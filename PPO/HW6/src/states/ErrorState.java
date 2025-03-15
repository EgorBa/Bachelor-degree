package states;

import resources.ErrorMessage;
import tokens.Token;

import java.util.List;

public class ErrorState extends State {
    @Override
    public State next(List<Token> list, String str, int pos) {
        System.err.printf(ErrorMessage.UNKNOWN_TOKEN, str.charAt(pos - 1), pos);
        list.clear();
        return this;
    }

}
