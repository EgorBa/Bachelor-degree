public abstract class AbstractMultiOperation implements Expression {
    protected Expression leftArgument, rightArgument;
    private int hash;
    protected String expr;

    AbstractMultiOperation(Expression leftArgument, Expression rightArgument) {
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
        hash = printExpresion().hashCode();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            AbstractMultiOperation obj1 = (AbstractMultiOperation) obj;
            return obj1.getType() == getType() && hashCode() == obj1.hashCode();
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
        return leftArgument;
    }

    @Override
    public Expression getRightPart() {
        return rightArgument;
    }
}
