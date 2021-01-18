package HW2;

import java.text.ParseException;
import java.util.HashMap;


public class Test {
    public static void main(String[] args) throws ParseException {
        Parser parser = new Parser();
        Tree tree = parser.parse("((a|(a*)*)*)*");
        //System.out.print(tree.printToGraphviz(new HashMap<>()));
        System.out.print(tree.printStr());
    }
}
