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
        if (argument.isConst()) {
            return String.format("!%s", argument.printExpresion());
        } else {
            return String.format("!(%s)", argument.printExpresion());
        }
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public int getType() {
        return 4;
    }
}