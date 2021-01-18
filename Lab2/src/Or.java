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
        return String.format("(%s | %s)", leftArgument.printExpresion(), rightArgument.printExpresion());
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