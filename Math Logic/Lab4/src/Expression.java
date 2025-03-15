public interface Expression {
    String printExpresionSpecial();
    String printExpresion();
    Expression getLeftPart();
    Expression getRightPart();
    boolean isConst();
    int getType();
    boolean equals(Object obj);
    boolean equals(Expression obj);
    int hashCode();
    int eval();
}