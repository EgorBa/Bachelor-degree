import java.io.*;
import java.util.*;

public class Main {

    private static final Character POINT = '.';
    private static final String TUR = "\\|-";
    private static Expression finalExpression;
    private static Expression check;
    private static boolean isFree;
    private static boolean notProved;
    private static boolean isWas;
    private static int[] numberProofs;
    private static HashMap<Integer, HashSet<Expression>> modusPonensRightPart;
    private static Expression lastExpr;
    private static ArrayList<String> proof;
    private static String exception;
    public static int count;
    public static boolean flag;
    public static boolean end;
    public static BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void main(String[] args) throws IOException {
        proof = new ArrayList<>();
        end = false;
        flag = false;
        notProved = false;
        numberProofs = new int[25647345];
        modusPonensRightPart = new HashMap<>();
        lastExpr = null;
        count = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader bufferedReader = new BufferedReader(new FileReader("in.in"));
        String statement;
        while ((statement = bufferedReader.readLine()) != null && !end && !notProved) {
            exception = null;
            addString(statement);
            flag = true;
        }
        if (notProved) {
            exception = (exception == null ? String.format("Expression %s is not proved.", count) : exception);
            end = true;
        }
        if (!end) {
            checkProofs();
        }
        bufferedReader.close();
        StringBuilder ans = new StringBuilder();
        int i = 0;
        for (String exp : proof) {
            ans.append(exp);
            //out.write(exp);
            if (i % 2 == 0) {
                ans.append('\n');
                //out.write('\n');
            }
            i++;
        }
        if (exception != null) {
            ans.append(exception);
            //out.write(exceptions.getFirst());
            //System.out.println(exceptions.getFirst());
        }
        out.write(ans.toString());
        out.close();
    }

    public static List<String> run(List<String> args) throws IOException {
        proof = new ArrayList<>();
        end = false;
        flag = false;
        notProved = false;
        numberProofs = new int[25647345];
        modusPonensRightPart = new HashMap<>();
        lastExpr = null;
        count = 0;
        for (String arg : args) {
            exception = null;
            addString(arg);
            if (end) break;
            if (notProved) break;
            flag = true;
        }
        if (notProved) {
            exception = (exception == null ? String.format("Expression %s is not proved.", count) : exception);
            end = true;
        }
        if (!end) {
            checkProofs();
        }
        LinkedList<String> ans = new LinkedList<>();
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String exp : proof) {
            str.append(exp);
            if (i % 2 == 0) {
                ans.add(str.toString());
                str = new StringBuilder();
            }
            i++;
            //System.out.println(exp.trim());
        }
        //System.out.println(exceptions);
        if (exception != null) {
            ans.add(exception);
        }
//        for (String st : ans) {
//            out.write(st);
//            out.write('\n');
//        }
//        out.close();
        return ans;
    }

    private static void parseFirstLine(String statement) {
        String[] split = statement.split(TUR);
        finalExpression = parse(split[1]);
        proof.add(TUR.substring(1) + finalExpression.printExpresion());
        //out.write(TUR.substring(1) + finalExpression.printExpresion() + '\n');
        count++;
    }

    private static boolean checkAxiom(Expression axiom) {
        int axiomNumber = getNumberAxiom(axiom);
        if (axiomNumber == 0) {
            return false;
        }
        if (axiomNumber <= 12 || axiomNumber == 21) {
            proof.add(String.format("[%s. Ax. sch. %s] ", count, axiomNumber != 21 ? axiomNumber : "A9"));
        } else {
            proof.add(String.format("[%s. Ax. %s] ", count, "A" + (axiomNumber - 12)));
        }
        addExpression(axiom);
        return true;
    }

    private static boolean checkModusPonens(Expression modusPonens) {
        if (!modusPonensRightPart.containsKey(modusPonens.hashCode())) {
            return false;
        }
        Expression ans = null;
        int first = -1;
        int second = -1;
        for (Expression expr : modusPonensRightPart.get(modusPonens.hashCode())) {
            if (expr.getType() == 1 && numberProofs[expr.getLeftPart().hashCode()] != 0) {
                if (ans == null) {
                    ans = modusPonens;
                    first = numberProofs[expr.getLeftPart().hashCode()];
                    second = numberProofs[expr.hashCode()];
                } else {
                    if (first < numberProofs[expr.getLeftPart().hashCode()]) {
                        first = numberProofs[expr.getLeftPart().hashCode()];
                        second = numberProofs[expr.hashCode()];
                    } else {
                        if (first == numberProofs[expr.getLeftPart().hashCode()] && second < numberProofs[expr.hashCode()]) {
                            first = numberProofs[expr.getLeftPart().hashCode()];
                            second = numberProofs[expr.hashCode()];
                        }
                    }
                }
            }
        }
        if (ans != null) {
            proof.add(String.format("[%s. M.P. %s, %s] ", count, first, second));
            addExpression(ans);
            return true;
        }
        return false;
    }

    private static boolean checkIntro(Expression intro) {
        if (intro.getType() == 1 && intro.getLeftPart().getType() == 6) {
            isWas = false;
            Expression find = new Implication(intro.getLeftPart().getRightPart(), intro.getRightPart());
            if (numberProofs[find.hashCode()] != 0) {
                if (!checkConsist(intro.getRightPart(), intro.getLeftPart().getLeftPart(), false) && isWas) {
                    exception = String.format("Expression %s: variable %s occurs free in ?@-rule.", count, intro.getLeftPart().getLeftPart().printExpresion());
                } else {
                    proof.add(String.format("[%s. ?@-intro %s] ", count, numberProofs[find.hashCode()]));
                    addExpression(intro);
                    return true;
                }
            }
        }
        if (intro.getType() == 1 && intro.getRightPart().getType() == 5) {
            Expression find = new Implication(intro.getLeftPart(), intro.getRightPart().getRightPart());
            if (numberProofs[find.hashCode()] != 0) {
                isWas = false;
                if (!checkConsist(intro.getLeftPart(), intro.getRightPart().getLeftPart(), false) && isWas) {
                    exception = String.format("Expression %s: variable %s occurs free in ?@-rule.", count, intro.getRightPart().getLeftPart().printExpresion());
                } else {
                    proof.add(String.format("[%s. ?@-intro %s] ", count, numberProofs[find.hashCode()]));
                    addExpression(intro);
                    return true;
                }
            }
        }
        return false;
    }

    private static void addExpression(Expression expression) {
        proof.add(expression.printExpresion());
        //out.write(expression.expression.printExpresion() + '\n');
        lastExpr = expression;
        if (expression.getType() == 1) {
            //intros.put(expression.expression, count);
            modusPonensRightPart.putIfAbsent(expression.getRightPart().hashCode(), new HashSet<>());
            modusPonensRightPart.get(expression.getRightPart().hashCode()).add(expression);
        }
        numberProofs[expression.hashCode()] = count;
        count++;
    }

    private static boolean checkConsist(Expression expression, Expression variable, boolean isClosed) {
        if (expression.getType() != 5 && expression.getType() != 6 && !expression.isConst()) {
            if (expression.getType() == 4 || expression.getType() == 11) {
                return checkConsist(expression.getRightPart(), variable, isClosed);
            } else {
                return checkConsist(expression.getRightPart(), variable, isClosed) && checkConsist(expression.getLeftPart(), variable, isClosed);
            }
        }
        if (expression.isConst()) {
            if (variable.equals(expression)) {
                isWas = true;
                return isClosed;
            } else {
                return true;
            }
        }
        isClosed = isClosed || variable.equals(expression.getLeftPart());
        return checkConsist(expression.getRightPart(), variable, isClosed);
    }

    public static void checkProofs() {
        if (!lastExpr.equals(finalExpression)) {
            exception = "The proof proves different expression.";
        } else {
            exception = null;
        }
    }

    public static void addString(String str) {
        //System.out.println(str);
        if (!flag) {
            parseFirstLine(str);
        } else {
            Expression expression = parse(str);
//            System.out.println(expression.printExpresion());
//            System.out.println(exp);
            if (checkAxiom(expression)) return;
            if (checkModusPonens(expression)) return;
            if (checkIntro(expression)) return;
            notProved = true;
        }
    }

    private static boolean check1Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getRightPart().getType() == 1)
                && (expression.getRightPart().getRightPart().equals(expression.getLeftPart()))
        );
    }

    private static boolean check2Axiom(Expression expression) {
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
    }

    private static boolean check3Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getRightPart().getType() == 1)
                && (expression.getRightPart().getRightPart().getType() == 2)
                && (expression.getLeftPart().equals(expression.getRightPart().getRightPart().getLeftPart()))
                && (expression.getRightPart().getLeftPart().equals(expression.getRightPart().getRightPart().getRightPart()))
        );
    }

    private static boolean check4Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getLeftPart().getType() == 2)
                && (expression.getLeftPart().getLeftPart().equals(expression.getRightPart()))
        );
    }

    private static boolean check5Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getLeftPart().getType() == 2)
                && (expression.getLeftPart().getRightPart().equals(expression.getRightPart()))
        );
    }

    private static boolean check6Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getRightPart().getType() == 3)
                && (expression.getLeftPart().equals(expression.getRightPart().getLeftPart()))
        );
    }

    private static boolean check7Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getRightPart().getType() == 3)
                && (expression.getLeftPart().equals(expression.getRightPart().getRightPart()))
        );
    }

    private static boolean check8Axiom(Expression expression) {
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
    }

    private static boolean check9Axiom(Expression expression) {
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
    }

    private static boolean check10Axiom(Expression expression) {
        return ((expression.getType() == 1)
                && (expression.getLeftPart().getType() == 4)
                && (expression.getLeftPart().getLeftPart().getType() == 4)
                && (expression.getLeftPart().getLeftPart().getLeftPart().equals(expression.getRightPart()))
        );
    }

    private static boolean check11Axiom(Expression expression) {
        check = null;
        isFree = true;
        return ((expression.getType() == 1)
                && (expression.getLeftPart().getType() == 5)
                && (checkEquals(expression.getLeftPart().getRightPart(), expression.getRightPart(), expression.getLeftPart().getLeftPart(), false, 0))
        );
    }

    private static boolean check12Axiom(Expression expression) {
        check = null;
        isFree = true;
        return ((expression.getType() == 1)
                && (expression.getRightPart().getType() == 6)
                && (checkEquals(expression.getRightPart().getRightPart(), expression.getLeftPart(), expression.getRightPart().getLeftPart(), false, 0))
        );
    }

    private static boolean checkEquals(Expression leftPart, Expression rightPart, Expression variable, boolean isClose, int bitMask) {
//        System.out.println(leftPart);
//        System.out.println(leftPart.printExpresion());
//        System.out.println(rightPart.printExpresion());
        if (leftPart.isConst()) {
            if (!isClose && variable.equals(leftPart)) {
                if (check == null) {
                    check = rightPart;
                    int mask = getMask(check);
                    isFree = (mask & bitMask) <= 0;
                } else {
                    return check.equals(rightPart);
                }
                return true;
            }
            return leftPart.equals(rightPart);
        }
        if (rightPart.getType() == leftPart.getType()) {
            if (rightPart.getType() == 5 || rightPart.getType() == 6) {
                if (variable.equals(leftPart.getLeftPart())) {
                    isClose = true;
                } else {
                    bitMask |= 1 << (((Variable) leftPart.getLeftPart()).name.charAt(0) - 97);
                }
            }
            if (rightPart.getType() == 4 || rightPart.getType() == 11) {
                return checkEquals(leftPart.getLeftPart(), rightPart.getLeftPart(), variable, isClose, bitMask);
            } else {
                return checkEquals(leftPart.getLeftPart(), rightPart.getLeftPart(), variable, isClose, bitMask) && checkEquals(leftPart.getRightPart(), rightPart.getRightPart(), variable, isClose, bitMask);
            }
        }
        return false;
    }

    private static boolean checkA1(Expression expression) {
        return expression.printExpresion().equals("((a=b)->((a=c)->(b=c)))");
    }

    private static boolean checkA2(Expression expression) {
        return expression.printExpresion().equals("((a=b)->(a'=b'))");
    }

    private static boolean checkA3(Expression expression) {
        return expression.printExpresion().equals("((a'=b')->(a=b))");
    }

    private static boolean checkA4(Expression expression) {
        return expression.printExpresion().equals("(!(a'=0))");
    }

    private static boolean checkA5(Expression expression) {
        return expression.printExpresion().equals("((a+0)=a)");
    }

    private static boolean checkA6(Expression expression) {
        return expression.printExpresion().equals("((a+b')=(a+b)')");
    }

    private static boolean checkA7(Expression expression) {
        return expression.printExpresion().equals("((a*0)=0)");
    }

    private static boolean checkA8(Expression expression) {
        return expression.printExpresion().equals("((a*b')=((a*b)+a))");
    }

    private static boolean checkA9(Expression expression) {
        check = null;
        if (!((expression.getType() == 1)
                && (expression.getLeftPart().getType() == 2)
                && (expression.getLeftPart().getRightPart().getType() == 5)
                && (expression.getLeftPart().getRightPart().getRightPart().getType() == 1)
                && (expression.getRightPart().equals(expression.getLeftPart().getRightPart().getRightPart().getLeftPart()))
        )) {
            return false;
        }
        Expression variable = expression.getLeftPart().getRightPart().getLeftPart();
        Expression newVariable = new Next(variable);
        Expression exp = expression.getRightPart();
        Expression exp1 = expression.getLeftPart().getLeftPart();
        Expression exp2 = expression.getLeftPart().getRightPart().getRightPart().getRightPart();
        return checkEqual(exp, exp1, variable, new Variable("0"), false) && checkEqual(exp, exp2, variable, newVariable, false);
    }

    private static boolean checkEqual(Expression exp, Expression exp1, Expression variable, Expression variable1, boolean isClosed) {
        if (exp.isConst()) {
            if (!isClosed && variable.equals(exp) && variable1.equals(exp1)) {
                return true;
            }
            if (isClosed && variable.equals(exp) && variable.equals(exp1)) {
                return true;
            }
            return !variable.equals(exp) && exp.equals(exp1);
        }
        if (exp.getType() == exp1.getType()) {
            if (exp.getType() == 5 || exp.getType() == 6) {
                isClosed = variable.equals(exp.getLeftPart());
            }
            if (exp.getType() == 4 || exp.getType() == 11) {
                return checkEqual(exp.getLeftPart(), exp1.getLeftPart(), variable, variable1, isClosed);
            } else {
                return checkEqual(exp.getLeftPart(), exp1.getLeftPart(), variable, variable1, isClosed) && checkEqual(exp.getRightPart(), exp1.getRightPart(), variable, variable1, isClosed);
            }
        }
        return false;
    }

    static public int getNumberAxiom(Expression expression) {
        if (check1Axiom(expression)) return 1;
        if (check2Axiom(expression)) return 2;
        if (check3Axiom(expression)) return 5;
        if (check4Axiom(expression)) return 3;
        if (check5Axiom(expression)) return 4;
        if (check6Axiom(expression)) return 6;
        if (check7Axiom(expression)) return 7;
        if (check8Axiom(expression)) return 8;
        if (check9Axiom(expression)) return 9;
        if (check10Axiom(expression)) return 10;
        if (check11Axiom(expression)) {
            if (check == null || isFree) {
                return 11;
            } else {
                exception = (exception == null ? String.format("Expression %s: variable %s is not free for term %s in ?@-axiom.", count, expression.getLeftPart().getLeftPart().printExpresion(), check.printExpresion()) : exception);
                return 0;
            }
        }
        if (check12Axiom(expression)) {
            if (check == null || isFree) {
                return 12;
            } else {
                exception = (exception == null ? String.format("Expression %s: variable %s is not free for term %s in ?@-axiom.", count, expression.getRightPart().getLeftPart().printExpresion(), check.printExpresion()) : exception);
                return 0;
            }
        }
        if (checkA9(expression)) return 21;
        if (checkA1(expression)) return 13;
        if (checkA2(expression)) return 14;
        if (checkA3(expression)) return 15;
        if (checkA4(expression)) return 16;
        if (checkA5(expression)) return 17;
        if (checkA6(expression)) return 18;
        if (checkA7(expression)) return 19;
        if (checkA8(expression)) return 20;
        return 0;
    }

    private static int getMask(Expression exp) {
        if (!exp.isConst()) {
            return getMask(exp.getLeftPart()) | getMask(exp.getRightPart());
        } else {
            if (exp.getType() == 10 && !exp.equals(new Variable("0"))) {
                return 1 << (((Variable) exp).name.charAt(0) - 97);
            } else {
                return 0;
            }
        }
    }

    private static String exp;
    private static int ind;
    private static boolean[] isOK;

    public static Expression parse(String expression) {
        //System.out.println(count + " " + expression);
        exp = expression;
        isOK = new boolean[expression.length()];
        initOk();
        ind = 0;
        Expression expr = parseImpl();
        //System.out.println(expr.printExpresion());
        return expr;
        // return parseImpl(stringToList());
    }

    private static void initOk() {
        int bal = 0;
        for (int i = exp.length() - 2; i >= 0; i--) {
            if (exp.charAt(i) == ')') {
                bal++;
            }
            if (exp.charAt(i) == '(') {
                bal--;
            }
            if (isOK[i + 1]) {
                if (bal >= 0 && (exp.charAt(i) != '&' && exp.charAt(i) != '|' && exp.charAt(i) != '>')) {
                    isOK[i] = true;
                }
            }
            if (exp.charAt(i) == '=') {
                isOK[i] = true;
                bal = 0;
            }
        }
    }

    public static Expression parseImpl() {
        Expression expr = parseDis();
        while (ind < exp.length() && exp.charAt(ind) == '-') {
            ind += 2;
            expr = new Implication(expr, parseImpl());
        }
        return expr;
    }

    public static Expression parseDis() {
        Expression expr = parseCon();
        LinkedList<Expression> list = new LinkedList<>();
        list.add(expr);
        while (ind < exp.length() && exp.charAt(ind) == '|') {
            ind++;
            Expression expression = parseCon();
            list.add(expression);
        }
        Expression buf;
        while (list.size() > 1) {
            Expression cur = list.getFirst();
            list.removeFirst();
            buf = new Or(cur, list.getFirst());
            list.removeFirst();
            list.addFirst(buf);
        }
        expr = list.getFirst();
        return expr;
    }

    public static Expression parseCon() {
        Expression expr = parseNeg();
        LinkedList<Expression> list = new LinkedList<>();
        list.add(expr);
        while (ind < exp.length() && exp.charAt(ind) == '&') {
            ind++;
            Expression expression = parseNeg();
            list.add(expression);
        }
        Expression buf;
        while (list.size() > 1) {
            Expression cur = list.getFirst();
            list.removeFirst();
            buf = new And(cur, list.getFirst());
            list.removeFirst();
            list.addFirst(buf);
        }
        expr = list.getFirst();
        return expr;
    }

    public static Expression parseNeg() {
        char chr = exp.charAt(ind);
        if (chr == '!') {
            ind++;
            return new Not(parseNeg());
        }
        if (chr == '@') {
            ind++;
            String str = String.valueOf(exp.charAt(ind));
            ind += 2;
            return new Forall(new Variable(str), parseImpl());
        }
        if (chr == '?') {
            ind++;
            String str = String.valueOf(exp.charAt(ind));
            ind += 2;
            return new Exists(new Variable(str), parseImpl());
        }
        if (chr == '(') {
            if (isOK[ind]) {
                return parsePredicate();
            } else {
                ind++;
                Expression expr = parseImpl();
                ind++;
                return expr;
            }
        } else {
            return parsePredicate();
        }
    }

    private static Expression parsePredicate() {
        char chr = exp.charAt(ind);
        if (chr <= 90 && chr >= 65) {
            ind++;
            return new Const(String.valueOf(chr));
        } else {
            Expression expr = parseTerm();
            ind++;
            return new Equals(expr, parseTerm());
        }
    }

    private static Expression parseTerm() {
        //System.out.println(exp.charAt(ind) + " " + ind);
        Expression expr = parseAdd();
        LinkedList<Expression> list = new LinkedList<>();
        list.add(expr);
        while (ind < exp.length() && exp.charAt(ind) == '+') {
            ind++;
            Expression expression = parseAdd();
            list.add(expression);
        }
        Expression buf;
        while (list.size() > 1) {
            Expression cur = list.getFirst();
            list.removeFirst();
            buf = new Add(cur, list.getFirst());
            list.removeFirst();
            list.addFirst(buf);
        }
        expr = list.getFirst();
        return expr;
    }

    private static Expression parseAdd() {
        Expression expr = parseMultiply();
        LinkedList<Expression> list = new LinkedList<>();
        list.add(expr);
        while (ind < exp.length() && exp.charAt(ind) == '*') {
            ind++;
            Expression expression = parseMultiply();
            list.add(expression);
        }
        Expression buf;
        while (list.size() > 1) {
            Expression cur = list.getFirst();
            list.removeFirst();
            buf = new Multiply(cur, list.getFirst());
            list.removeFirst();
            list.addFirst(buf);
        }
        expr = list.getFirst();
        return expr;
    }

    private static Expression parseMultiply() {
        char chr = exp.charAt(ind);
        Expression expr = null;
        if (chr == '0') {
            ind++;
            expr = new Variable("0");
        }
        if (chr >= 97) {
            ind++;
            expr = new Variable(String.valueOf(chr));
        }
        if (chr == '(') {
            ind++;
            expr = parseTerm();
            ind++;
        }
        while (ind < exp.length() && exp.charAt(ind) == '\'') {
            ind++;
            expr = new Next(expr);
        }
        return expr;
    }

}