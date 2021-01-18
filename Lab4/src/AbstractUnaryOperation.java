public abstract class AbstractUnaryOperation implements Expression {
    protected Expression argument;
    protected String expr;
    private int hash;

    AbstractUnaryOperation(Expression argument) {
        this.argument = argument;
        hash = printExpresion().hashCode();
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
