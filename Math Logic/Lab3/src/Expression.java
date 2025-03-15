import java.util.ArrayList;
import java.util.LinkedList;

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
    void getAllStrings(ArrayList<String> expr);
}