import java.util.ArrayList;
import java.util.LinkedList;

public abstract class AbstractUnaryOperation implements Expression {
    protected Expression argument;
    protected String expr;
    private int hash;

    AbstractUnaryOperation(Expression argument) {
        this.argument = argument;
        hash = (getLeftPart().hashCode() * (getType() + 37) + 454354) % 25647345;
        hash = (hash < 0 ? -hash : hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            AbstractUnaryOperation obj1 = (AbstractUnaryOperation) obj;
            return this.getType() == obj1.getType() && hashCode() == obj1.hashCode();
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String printExpresion() {
        if (expr == null) {
            ArrayList<String> strs = new ArrayList<>();
            getAllStrings(strs);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : strs) {
                stringBuilder.append(str);
            }
            expr = stringBuilder.toString();
        }
        return expr;
    }

    @Override
    public boolean equals(Expression obj) {
        if (obj.getType() == getType()) {
            return hashCode() == obj.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public Expression getLeftPart() {
        return argument;
    }

    @Override
    public Expression getRightPart() {
        return argument;
    }
}
