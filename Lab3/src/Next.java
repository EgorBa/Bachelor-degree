import java.util.ArrayList;
import java.util.LinkedList;

public class Next extends AbstractUnaryOperation {

    Next(Expression argument) {
        super(argument);
    }

    @Override
    public String printExpresionSpecial() {
        return null;
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public int getType() {
        return 11;
    }

    @Override
    public void getAllStrings(ArrayList<String> expr) {
        argument.getAllStrings(expr);
        expr.add("'");
    }
}
