public interface Expression {
    String printExpresionSpecial();
    String printExpresion();
    Expression getLeftPart();
    Expression getRightPart();
    boolean isConst();
    int getType();
    boolean equals(Object obj);
    int hashCode();
}