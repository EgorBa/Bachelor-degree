public final class Or extends AbstractMultiOperation {

    public Or(Expression leftArgument, Expression rightArgument) {
        super(leftArgument, rightArgument);
    }

    @Override
    public String printExpresionSpecial() {
        return String.format("(|,%s,%s)", leftArgument.printExpresionSpecial(), rightArgument.printExpresionSpecial());
    }

    @Override
    public String printExpresion() {
        return expr == null ? expr = String.format("(%s|%s)", leftArgument.printExpresion(), rightArgument.printExpresion()) : expr;
    }

    @Override
    public int eval() {
        return (leftArgument.eval() == 0 && rightArgument.eval() == 0) ? 0 : 1;
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public int getType() {
        return 3;
    }
}