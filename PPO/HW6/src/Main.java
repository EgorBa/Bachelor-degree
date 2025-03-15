import tokens.Token;
import visitors.CalcVisitor;
import visitors.ParseVisitor;
import visitors.PrintVisitor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer("(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2");
        List<Token> tokens = tokenizer.parse();

        ParseVisitor parseVisitor = new ParseVisitor();
        List<Token> tokenInRPN = parseVisitor.toRPN(tokens);

        PrintVisitor printVisitor = new PrintVisitor();
        System.out.println(printVisitor.print(tokenInRPN));

        CalcVisitor calcVisitor = new CalcVisitor(tokenInRPN);
        System.out.println(calcVisitor.calc());
    }
}
