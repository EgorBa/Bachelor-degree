public abstract class AbstractMultiOperation implements Expression {
    protected Expression leftArgument, rightArgument;

    AbstractMultiOperation(Expression leftArgument, Expression rightArgument) {
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
    }

    @Override
    public int hashCode() {
        return ((leftArgument.hashCode() + rightArgument.hashCode()) * this.getType() * 4547) % 3544325 + 6543;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            AbstractMultiOperation obj1 = (AbstractMultiOperation) obj;
            return leftArgument.equals(obj1.leftArgument) && rightArgument.equals(obj1.rightArgument) && this.getType() == obj1.getType();
        } catch (ClassCastException e) {
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
