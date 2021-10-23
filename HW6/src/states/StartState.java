package states;

import tokens.Brace;
import tokens.Token;
import tokens.Operation;

import java.util.List;

public class StartState extends State {
    @Override
    public State next(List<Token> list, String str, int pos) {
        if (pos >= str.length()) {
            return new EndState();
        }
        char chr = str.charAt(pos);
        pos++;
        if (isDigit(chr)) {
            return new NumberState(chr).next(list, str, pos);
        } else if (isBrace(chr)) {
            list.add(new Brace(chr));
            return this.next(list, str, pos);
        } else if (isSkip(chr)) {
            return this.next(list, str, pos);
        } else if (isOp(chr)) {
            list.add(new Operation(chr));
            return this.next(list, str, pos);
        } else return new ErrorState().next(list, str, pos);
    }
}
