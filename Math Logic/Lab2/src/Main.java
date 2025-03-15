import java.io.*;
import java.util.*;

public class Main {

    public static class Pair {

        public Expression expression;
        public int number;

        Pair(Expression exp, int n) {
            expression = exp;
            number = n;
        }

    }

    public static class AnyExpression {

        public Expression expression;
        public int numberHypothesise = 0;
        public int numberAxiom = 0;
        public int numberModusPonensFirst, numberModusPonensPSecond;
        public boolean isHypothesise = false;
        public boolean isAxiom = false;
        public boolean isMP = false;
        public boolean isResultProof = false;

        public AnyExpression(Expression expression) {
            this.expression = expression;
        }

    }

    private static final String COMMA = ",";
    private static final String EMPTY = "";
    private static final String TUR = "\\|-";
    private static Expression finalExpression;
    private static List<Pair> hypotheses;
    private static HashMap<Expression, Integer> numberProofs;
    private static HashMap<Expression, LinkedList<Expression>> modusPonensRightPart;
    private static List<AnyExpression> proofs;
    public static int count;
    public static boolean flag;

    public static void main(String[] args) throws IOException
    {
        flag = false;
        hypotheses = new ArrayList<>();
        numberProofs = new HashMap<>();
        modusPonensRightPart = new HashMap<>();
        proofs = new ArrayList<>();
        count = 0;
        //BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Egor Bazhenov\\IdeaProjects\\ProofChecker\\test3.in"));
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String statement;
            while ((statement = bufferedReader.readLine()) != null) {
                addString(statement);
                flag = true;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Proof is incorrect");
            return;
        }
        checkProofs();
        writeProofs();
    }

    private static void parseFirstLine(String statement) {
        String[] split = statement.split(TUR);
        int i = 1;
        for (String s : split[0].split(COMMA)) {
            if (s.equals(EMPTY)) {
                continue;
            }
            hypotheses.add(new Pair(parse(s), i));
            i++;
        }
        finalExpression = parse(split[1]);
        count++;
    }

    private static boolean checkHypothesise(Expression hypothesise) {
        for (Pair pair : hypotheses) {
            if (pair.expression.equals(hypothesise)) {
                AnyExpression expression = new AnyExpression(hypothesise);
                expression.isHypothesise = true;
                expression.numberHypothesise = pair.number;
                addExpression(expression);
                return true;
            }
        }
        return false;
    }

    private static boolean checkAxiom(Expression axiom) {
        int axiomNumber = getNumberAxiom(axiom);
        if (axiomNumber == 0) {
            return false;
        }
        AnyExpression expression = new AnyExpression(axiom);
        expression.isAxiom = true;
        expression.numberAxiom = axiomNumber;
        addExpression(expression);
        return true;
    }

    private static boolean checkModusPonens(Expression modusPonens) {
        if (!modusPonensRightPart.containsKey(modusPonens)) {
            return false;
        }
        for (Expression expr : modusPonensRightPart.get(modusPonens)) {
            if (expr.getType() == 1 && numberProofs.containsKey(expr.getLeftPart())) {
                AnyExpression expression = new AnyExpression(modusPonens);
                expression.isMP = true;
                expression.numberModusPonensFirst = numberProofs.get(expr.getLeftPart());
                expression.numberModusPonensPSecond = numberProofs.get(expr);
                addExpression(expression);
                return true;
            }
        }
        return false;
    }

    private static void addExpression(AnyExpression expression) {
        proofs.add(expression);
        if (expression.expression.getType() == 1) {
            modusPonensRightPart.putIfAbsent(expression.expression.getRightPart(), new LinkedList<>());
            modusPonensRightPart.get(expression.expression.getRightPart()).addFirst(expression.expression);
        }
        numberProofs.putIfAbsent(expression.expression, count);
        count++;
    }

    public static void checkProofs() {
        if (proofs.get(proofs.size() - 1).expression.equals(finalExpression)) {
            for (int i = 0; i <= proofs.size(); i++) {
                if (proofs.get(i).expression.equals(finalExpression)) {
                    checkProof(proofs.get(i));
                    return;
                }
            }
        }
        System.out.println("Proof is incorrect");
        System.exit(0);
    }

    private static void checkProof(AnyExpression expression) {
        expression.isResultProof = true;
        if (expression.isMP) {
            checkProof(proofs.get(expression.numberModusPonensFirst - 1));
            checkProof(proofs.get(expression.numberModusPonensPSecond - 1));
        }
    }

    public static void writeProofs() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair hypothesis : hypotheses) {
            stringBuilder.append(hypothesis.expression.printExpresion()).append(", ");
        }
        if (hypotheses.size() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));
            stringBuilder.append(" ");
        }
        PrintWriter out = new PrintWriter(System.out);
        stringBuilder.append("|- ").append(finalExpression.printExpresion()).append(System.lineSeparator());
        out.print(stringBuilder.toString());
        int counter = 1;
        Map<Integer, Integer> indexes = new HashMap<>();
        for (AnyExpression expression : proofs) {
            if (expression.isResultProof) {
                out.print("[" + counter + ". ");
                indexes.put(numberProofs.get(expression.expression), counter);
                counter++;
                if (expression.isHypothesise) {
                    out.println("Hypothesis " + expression.numberHypothesise + "] " + expression.expression.printExpresion());
                }
                if (expression.isAxiom) {
                    out.println("Ax. sch. " + expression.numberAxiom + "] " + expression.expression.printExpresion());
                }
                if (expression.isMP) {
                    out.println("M.P. " + indexes.get(expression.numberModusPonensPSecond) + ", " + indexes.get(expression.numberModusPonensFirst) + "] " + expression.expression.printExpresion());
                }
            }
        }
        out.close();
    }

    public static void addString(String str) {
        if (!flag) {
            parseFirstLine(str);
        } else {
            Expression expression = parse(str);
            if (checkHypothesise(expression)) return;
            if (checkAxiom(expression)) return;
            if (checkModusPonens(expression)) return;
            throw new IllegalArgumentException();
        }
    }

    private static boolean check1Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getRightPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().equals(expression.getLeftPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check2Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 1)
                    && (expression.getRightPart().getType() == 1)
                    && (expression.getRightPart().getLeftPart().getType() == 1)
                    && (expression.getRightPart().getLeftPart().getRightPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().getType() == 1)
                    && (expression.getLeftPart().getLeftPart().equals(expression.getRightPart().getLeftPart().getLeftPart()))
                    && (expression.getRightPart().getLeftPart().getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart()))
                    && (expression.getLeftPart().getRightPart().equals(expression.getRightPart().getLeftPart().getRightPart().getLeftPart()))
                    && (expression.getRightPart().getLeftPart().getRightPart().getRightPart().equals(expression.getRightPart().getRightPart().getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check3Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getRightPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().getType() == 2)
                    && (expression.getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart()))
                    && (expression.getRightPart().getLeftPart().equals(expression.getRightPart().getRightPart().getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check4Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 2)
                    && (expression.getLeftPart().getLeftPart().equals(expression.getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check5Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 2)
                    && (expression.getLeftPart().getRightPart().equals(expression.getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check6Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getRightPart().getType() == 3)
                    && (expression.getLeftPart().equals(expression.getRightPart().getLeftPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check7Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getRightPart().getType() == 3)
                    && (expression.getLeftPart().equals(expression.getRightPart().getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check8Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 1)
                    && (expression.getRightPart().getType() == 1)
                    && (expression.getRightPart().getLeftPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().getLeftPart().getType() == 3)
                    && (expression.getLeftPart().getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart().getLeftPart()))
                    && (expression.getRightPart().getLeftPart().getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart().getRightPart()))
                    && (expression.getLeftPart().getRightPart().equals(expression.getRightPart().getLeftPart().getRightPart()))
                    && (expression.getRightPart().getLeftPart().getRightPart().equals(expression.getRightPart().getRightPart().getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check9Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 1)
                    && (expression.getRightPart().getType() == 1)
                    && (expression.getRightPart().getLeftPart().getType() == 1)
                    && (expression.getRightPart().getRightPart().getType() == 4)
                    && (expression.getRightPart().getLeftPart().getRightPart().getType() == 4)
                    && (expression.getLeftPart().getLeftPart().equals(expression.getRightPart().getLeftPart().getLeftPart()))
                    && (expression.getRightPart().getLeftPart().getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart()))
                    && (expression.getLeftPart().getRightPart().equals(expression.getRightPart().getLeftPart().getRightPart().getLeftPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean check10Axiom(Expression expression) {
        try {
            return ((expression.getType() == 1)
                    && (expression.getLeftPart().getType() == 4)
                    && (expression.getLeftPart().getLeftPart().getType() == 4)
                    && (expression.getLeftPart().getLeftPart().getLeftPart().equals(expression.getRightPart()))
            );
        } catch (Exception e) {
            return false;
        }
    }

    static public int getNumberAxiom(Expression expression) {
        if (check1Axiom(expression)) return 1;
        if (check2Axiom(expression)) return 2;
        if (check3Axiom(expression)) return 3;
        if (check4Axiom(expression)) return 4;
        if (check5Axiom(expression)) return 5;
        if (check6Axiom(expression)) return 6;
        if (check7Axiom(expression)) return 7;
        if (check8Axiom(expression)) return 8;
        if (check9Axiom(expression)) return 9;
        if (check10Axiom(expression)) return 10;
        return 0;
    }

    public static Expression parse(String expression) {
        return parseImpl(normalise(expression));
    }

    public static Expression parseImpl(String expression) {
        int balance = 0;
        char chr;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            switch (chr = expression.charAt(i)) {
                case ')':
                    balance--;
                    str.insert(0, chr);
                    continue;
                case '(':
                    balance++;
                    str.insert(0, chr);
                    continue;
                default:
                    if (chr == '-' && balance == 0) {
                        return new Implication(parseDis(str.toString()), parseImpl(expression.substring(i + 2)));
                    } else {
                        str.insert(0, chr);
                    }
            }
        }
        return parseDis(str.toString());
    }

    public static Expression parseDis(String expression) {
        int balance = 0;
        char chr;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            switch (chr = expression.charAt(i)) {
                case ')':
                    balance--;
                    str.insert(0, chr);
                    continue;
                case '(':
                    balance++;
                    str.insert(0, chr);
                    continue;
                default:
                    if (chr == '|' && balance == 0) {
                        return new Or(parseDis(expression.substring(i + 1)), parseCon(str.toString()));
                    } else {
                        str.insert(0, chr);
                    }
            }
        }
        return parseCon(str.toString());
    }

    public static String recursiveReverse(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            str.insert(0, s.charAt(i));
        }
        return str.toString();
    }

    public static Expression parseCon(String expression) {
        expression = recursiveReverse(expression);
        int balance = 0;
        char chr;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            switch (chr = expression.charAt(i)) {
                case ')':
                    balance--;
                    str.insert(0, chr);
                    continue;
                case '(':
                    balance++;
                    str.insert(0, chr);
                    continue;
                default:
                    if (chr == '&' && balance == 0) {
                        return new And(parseCon(recursiveReverse(expression.substring(i + 1))), parseNeg(str.toString()));
                    } else {
                        str.insert(0, chr);
                    }
            }
        }
        return parseNeg(str.toString());
    }

    public static Expression parseNeg(String expression) {
        char chr = expression.charAt(0);
        if (chr == '!') {
            return new Not(parseNeg(expression.substring(1)));
        } else {
            if (chr == '(') {
                return parseImpl(expression.substring(1, expression.length() - 1));
            } else {
                return new Const(expression);
            }
        }
    }

    public static String normalise(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                str.append(s.charAt(i));
            }
        }
        return str.toString();
    }

}