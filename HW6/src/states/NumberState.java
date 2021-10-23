package states;

import tokens.Token;
import tokens.Brace;
import tokens.NumberToken;
import tokens.Operation;

import java.util.List;

public class NumberState extends State {
    private final String prefix;

    NumberState(String p) {
        prefix = p;
    }

    NumberState(char p) {
        prefix = String.valueOf(p);
    }

    @Override
    public State next(List<Token> list, String str, int pos) {
        if (pos >= str.length()) {
            list.add(new NumberToken(prefix));
            return new EndState();
        }
        char chr = str.charAt(pos);
        pos++;
        if (isDigit(chr)) {
            return new NumberState(prefix + chr).next(list, str, pos);
        } else {
            list.add(new NumberToken(prefix));
            if (isBrace(chr)) {
                list.add(new Brace(chr));
                return new StartState().next(list, str, pos);
            } else if (isSkip(chr)) {
                return new StartState().next(list, str, pos);
            } else if (isOp(chr)) {
                list.add(new Operation(chr));
                return new StartState().next(list, str, pos);
            } else {
                return new ErrorState().next(list, str, pos);
            }
        }
    }
}
