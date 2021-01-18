import java.util.ArrayList;
import java.util.LinkedList;

public class Variable implements Expression {

    public String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Variable obj1 = (Variable) obj;
            return printExpresion().equals(obj1.printExpresion());
        } catch (ClassCastException e) {
            return false;
        }
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
    public int hashCode() {
        return name.charAt(0);
    }

    @Override
    public void getAllStrings(ArrayList<String> expr) {
        expr.add(name);
    }

    @Override
    public String printExpresionSpecial() {
        return name;
    }

    @Override
    public String printExpresion() {
        return name;
    }

    @Override
    public Expression getLeftPart() {
        return null;
    }

    @Override
    public Expression getRightPart() {
        return null;
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public int getType() {
        return 10;
    }
}
