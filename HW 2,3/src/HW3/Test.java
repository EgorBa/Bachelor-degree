package HW3;

import com.antr.CalcLexer;
import com.antr.CalcParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder str = new StringBuilder();
        while (true) {
            String buf = in.readLine();
            str.append(buf);
            if (buf.length() == 0) {
                break;
            }
        }
        CalcLexer lexer = new CalcLexer(CharStreams.fromString(str.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcParser parser = new CalcParser(tokens);
        parser.calc();
    }
}
