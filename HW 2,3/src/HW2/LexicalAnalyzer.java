package HW2;

import java.text.ParseException;

public class LexicalAnalyzer {
    String is;
    int curChar;
    int curPos;
    Token curToken;

    public LexicalAnalyzer(String is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
        return c == '\r' || c == '\n' || c == '\t';
    }

    private void nextChar() throws ParseException {
        try {
            curChar = is.charAt(curPos);
        } catch (StringIndexOutOfBoundsException e) {
            curChar = -1;
        }
        curPos++;
    }

    public void nextToken() throws ParseException {
        while (isBlank(curChar)) {
            nextChar();
        }
        switch (curChar) {
            case '(':
                nextChar();
                curToken = Token.LPAREN;
                break;
            case ')':
                nextChar();
                curToken = Token.RPAREN;
                break;
            case '*':
                nextChar();
                curToken = Token.KLEENE;
                break;
            case '|':
                nextChar();
                curToken = Token.CHOOSE;
                break;
            case -1:
                curToken = Token.END;
                break;
            default:
                if (curChar >= 'a' && curChar <= 'z') {
                    nextChar();
                    curToken = Token.CHAR;
                } else {
                    throw new ParseException("Illegal character "
                            + (char) curChar, curPos);
                }
        }
    }

    public Token curToken() {
        return curToken;
    }

    public char prevChar() {
        return is.charAt(curPos - 2);
    }

    public int curPos() {
        return curPos;
    }
}

