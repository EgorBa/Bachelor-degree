package HW2;

import org.junit.Test;

import java.text.ParseException;
import java.util.Random;

import static junit.framework.TestCase.*;

public class UnitTests {
    private static final Parser parser = new Parser();

    @Test
    public void simpleTests() throws ParseException {
        //тесы на базовые правила
        assertEquals(parser.parse("a").printStr(), "a");
        assertEquals(parser.parse("ab").printStr(), "ab");
        assertEquals(parser.parse("a|b").printStr(), "a|b");
        assertEquals(parser.parse("a*").printStr(), "a*");
        assertEquals(parser.parse("(a)").printStr(), "(a)");
    }

    @Test
    public void repeatTests() throws ParseException {
        //тесты на продолжения базовых правил
        assertEquals(parser.parse("aaaa").printStr(), "aaaa");
        assertEquals(parser.parse("a|a|a|a").printStr(), "a|a|a|a");
        //assertEquals(parser.parse("a****").printStr(), "a****");
    }

    @Test
    public void nestingTests() throws ParseException {
        //тесты на вложенность
       // assertEquals(parser.parse("((a*)**)").printStr(), "((a*)**)");
        assertEquals(parser.parse("((a|(a)))").printStr(), "((a|(a)))");
        assertEquals(parser.parse("(((a(a)a)))").printStr(), "(((a(a)a)))");
        assertEquals(parser.parse("(((a)))").printStr(), "(((a)))");
    }

    @Test
    public void emptyTest() throws ParseException {
        //тест на пустую строку
        assertEquals(parser.parse("").printStr(), "");
    }

    @Test(expected = ParseException.class)
    public void errorWithoutCloseBracketTest() throws ParseException {
        //тест на отсутсвие закрывающей скобки
        parser.parse("(a");
    }

    @Test(expected = ParseException.class)
    public void errorUnknownTokenTest() throws ParseException {
        //тест на непонятные символы
        parser.parse("1223");
    }

    private String generateRandomStr(int length) {
        Random random = new Random();
        StringBuilder str = new StringBuilder("a");
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(4);
            switch (r) {
                case 0: {
                    str.append("a");
                    break;
                }
                case 1: {
                    str.append("|a");
                    break;
                }
                case 2: {
                    str.append("*");
                    break;
                }
                case 3: {
                    str = new StringBuilder("(" + str + ")");
                    break;
                }
            }
        }
        return str.toString();
    }

    @Test
    public void randomTest() throws ParseException {
        //рандомные тесты разной длины
        String str = generateRandomStr(10);
        assertEquals(parser.parse(str).printStr(), str);
        str = generateRandomStr(30);
        assertEquals(parser.parse(str).printStr(), str);
        str = generateRandomStr(50);
        assertEquals(parser.parse(str).printStr(), str);
    }

}