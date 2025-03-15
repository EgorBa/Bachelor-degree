package HW3;

import com.antr.CalcLexer;
import com.antr.CalcParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

public class UnitTests {

    @Test
    public void simpleTest1() {
        parse("a = 2 ; \n" +
                "b = a + 2;" +
                "c = a + b * (b - 3);" +
                "a = 3;" +
                "c = a + b * (b - 3);");
    }

    @Test
    public void simpleTest2() {
        parse("a = -2;" +
                "b = a + 2;" +
                "c = a + -b * (b - 3);" +
                "a = -3;" +
                "c = a + b * (b - 3);");
    }

    @Test
    public void simpleLogTest() {
        parse("b = 16 // 4 // 2;" +
                "a = 16 // (4//2);");
    }

    @Test(expected = ArithmeticException.class)
    public void errorDivisionByZero() {
        parse("a = 0;" +
                "b = 2/a;");
    }

    public void parse(String str){
        CalcLexer lexer = new CalcLexer(CharStreams.fromString(str));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcParser parser = new CalcParser(tokens);
        parser.calc();
    }
}
