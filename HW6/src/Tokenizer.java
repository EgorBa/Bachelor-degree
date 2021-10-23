import states.StartState;
import tokens.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private final String data;

    Tokenizer(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine());
        }
        data = stringBuilder.toString();
    }

    Tokenizer(String str) {
        data = str;
    }

    public List<Token> parse() {
        List<Token> tokens = new ArrayList<>();
        new StartState().next(tokens, data, 0);
        return tokens;
    }

}
