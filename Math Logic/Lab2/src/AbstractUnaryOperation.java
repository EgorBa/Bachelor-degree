public abstract class AbstractUnaryOperation implements Expression {
    protected Expression argument;

    AbstractUnaryOperation(Expression argument) {
        this.argument = argument;
    }

    @Override
    public int hashCode() {
        return (argument.hashCode() * 8) % 3243245 + 5432;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            AbstractUnaryOperation obj1 = (AbstractUnaryOperation) obj;
            return argument.equals(obj1.argument) && this.getType() == obj1.getType();
        } catch (ClassCastException e) {
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
