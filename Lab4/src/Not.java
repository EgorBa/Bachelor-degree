public final class Not extends AbstractUnaryOperation {

    public Not(Expression argument) {
        super(argument);
    }

    @Override
    public String printExpresionSpecial() {
        return String.format("(!%s)", argument.printExpresionSpecial());
    }

    @Override
    public String printExpresion() {
        if (expr == null) {
            return expr = String.format("(!%s)", argument.printExpresion());
        }
        return expr;
    }

    @Override
    public int eval() {
        return argument.eval() == 1 ? 0 : 1;
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public int getType() {
        return 4;
    }
}