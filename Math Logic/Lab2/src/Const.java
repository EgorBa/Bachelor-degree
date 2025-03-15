public class Const implements Expression {
    private String name;

    public Const(String name) {
        this.name = name;
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
    public int hashCode() {
        return name.hashCode();
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
