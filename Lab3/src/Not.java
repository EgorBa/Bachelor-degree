import java.util.ArrayList;
import java.util.LinkedList;

public final class Not extends AbstractUnaryOperation {

    public Not(Expression argument) {
        super(argument);
    }

    @Override
    public String printExpresionSpecial() {
        return String.format("(!%s)", argument.printExpresionSpecial());
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public void getAllStrings(ArrayList<String> expr) {
        expr.add("(!");
        argument.getAllStrings(expr);
        expr.add(")");
    }
}