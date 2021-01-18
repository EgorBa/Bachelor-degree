public class Const implements Expression {

    private String name;
    private int hash;
    public int value;

    public Const(String name) {
        this.name = name;
        hash = name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Const obj1 = (Const) obj;
            return name.equals(obj1.name);
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
        return hash;
    }

    @Override
    public int eval() {
        return value > 0 ? 1 : 0;
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
        return 0;
    }
}
