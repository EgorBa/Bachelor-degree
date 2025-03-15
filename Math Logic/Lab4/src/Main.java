import java.io.*;
import java.util.*;

public class Main {

    private static ArrayList<Expression> consts;
    private static HashMap<Expression, LinkedList<Expression>> modusPonensRightPart;
    private static HashSet<Expression> allImpl;
    private static ArrayList<Expression> hypo;

    public static void main(String[] args) throws IOException {
        hypo = new ArrayList<>();
        consts = new ArrayList<>();
        ArrayList<LinkedList<Expression>> listOfProof = new ArrayList<>();
        ArrayList<LinkedList<Expression>> listOfVars = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader bufferedReader = new BufferedReader(new FileReader("in.in"));
        Expression finalExpression = parse(bufferedReader.readLine());
        LinkedList<Expression>[] proofs = new LinkedList[1 << consts.size()];
        int[] result = new int[1 << consts.size()];
        for (int i = 0; i < 1 << consts.size(); i++) {
            for (int j = 0; j < consts.size(); j++) {
                Expression con = consts.get(j);
                ((Const) con).value = (1 << j) & i;
            }
            result[i] = finalExpression.eval();
        }
        if (findHypo(result)) {
            int hip = 0;
            for (int i = 0; i < consts.size(); i++) {
                if (hypo.contains(consts.get(i))) {
                    hip += 1 << i;
                }
            }
            for (int i = 0; i < 1 << consts.size(); i++) {
                int c = 0;
                LinkedList<Expression> cur = new LinkedList<>();
                for (int j = 0; j < consts.size(); j++) {
                    Expression con = consts.get(j);
                    ((Const) con).value = (1 << j) & i;
                    if (con.eval() == 1) {
                        cur.add(con);
                        c += 1 << j;
                    } else {
                        cur.add(new Not(con));
                    }
                }
                if ((c & hip) == hip) {
                    proof(finalExpression, proofs[i] = new LinkedList<>());
                    listOfVars.add(cur);
                    listOfProof.add(proofs[i]);
                }
            }
            while (listOfProof.size() != 1) {
                ArrayList<LinkedList<Expression>> ans = new ArrayList<>();
                ArrayList<LinkedList<Expression>> newVars = new ArrayList<>();
                for (int i = 0; i < listOfProof.size(); i += 2) {
                    LinkedList<Expression> list1 = listOfVars.get(i);
                    LinkedList<Expression> list2 = listOfVars.get(i + 1);
                    Expression var = null;
                    for (int j = 0; j < list1.size(); j++) {
                        if (!list1.get(j).equals(list2.get(j))) {
                            var = list2.get(j);
                        }
                    }
                    list1.remove(new Not(var));
                    list2.remove(var);
                    ans.add(merge(deductProof(listOfProof.get(i), new Not(var), list1), deductProof(listOfProof.get(i + 1), var, list2)));
                    newVars.add(list1);
                }
                listOfVars = newVars;
                listOfProof = ans;
            }
        } else {
            finalExpression = new Not(finalExpression);
            for (int i = 0; i < 1 << consts.size(); i++) {
                for (int j = 0; j < consts.size(); j++) {
                    Expression con = consts.get(j);
                    ((Const) con).value = ((1 << j) & i) > 0 ? 0 : 1;
                }
                result[i] = finalExpression.eval();
            }
            if (findHypo(result)) {
                int hip = 0;
                for (int i = 0; i < consts.size(); i++) {
                    if (hypo.contains(consts.get(i))) {
                        hip += 1 << i;
                    }
                }
                HashSet<Expression> list = new HashSet<>(hypo);
                hypo = new ArrayList<>();
                for (Expression exp : list) {
                    hypo.add(new Not(exp));
                }
                for (int i = (1 << consts.size()) - 1; i >= 0; i--) {
                    int c = 0;
                    LinkedList<Expression> cur = new LinkedList<>();
                    for (int j = 0; j < consts.size(); j++) {
                        Expression con = consts.get(j);
                        ((Const) con).value = ((1 << j) & i) > 0 ? 0 : 1;
                        if (con.eval() == 1) {
                            cur.add(con);
                        } else {
                            cur.add(new Not(con));
                            c += 1 << j;
                        }
                    }
                    if ((c & hip) == hip) {
                        proof(finalExpression, proofs[i] = new LinkedList<>());
                        listOfVars.add(cur);
                        listOfProof.add(proofs[i]);
                    }
                }
                while (listOfProof.size() != 1) {
                    ArrayList<LinkedList<Expression>> ans = new ArrayList<>();
                    ArrayList<LinkedList<Expression>> newVars = new ArrayList<>();
                    for (int i = 0; i < listOfProof.size(); i += 2) {
                        LinkedList<Expression> list1 = listOfVars.get(i);
                        LinkedList<Expression> list2 = listOfVars.get(i + 1);
                        Expression var = null;
                        for (int j = 0; j < list1.size(); j++) {
                            if (!list1.get(j).equals(list2.get(j))) {
                                var = list2.get(j);
                            }
                        }
                        list1.remove(new Not(var));
                        list2.remove(var);
                        ans.add(merge(deductProof(listOfProof.get(i), new Not(var), list1), deductProof(listOfProof.get(i + 1), var, list2)));
                        newVars.add(list1);
                    }
                    listOfVars = newVars;
                    listOfProof = ans;
                }
            } else {
                System.out.println(":(");
                return;
            }
        }
        Iterator<Expression> iter = hypo.iterator();
        for (int i = 0; i < hypo.size(); i++) {
            if (i == hypo.size() - 1) {
                System.out.print(iter.next().printExpresion() + " ");
            } else {
                System.out.print(iter.next().printExpresion() + ", ");
            }
        }
        System.out.println("|- " + finalExpression.printExpresion());
        listOfProof.forEach(list -> list.forEach(a -> System.out.println(a.printExpresion())));
    }

    private static LinkedList<Expression> merge(LinkedList<Expression> list1, LinkedList<Expression> list2) {
        LinkedList<Expression> ans = new LinkedList<>();
        ans.addAll(list1);
        ans.addAll(list2);
        if (list1.getLast().getLeftPart().getType() == 4) {
            LinkedList<Expression> buf = list1;
            list1 = list2;
            list2 = buf;
        }
        ans.addLast(new Implication(list1.getLast(), new Implication(list2.getLast(), new Implication(new Or(list1.getLast().getLeftPart(), list2.getLast().getLeftPart()), list1.getLast().getRightPart()))));
        ans.addLast(new Implication(list2.getLast(), new Implication(new Or(list1.getLast().getLeftPart(), list2.getLast().getLeftPart()), list1.getLast().getRightPart())));
        ans.addLast(new Implication(new Or(list1.getLast().getLeftPart(), list2.getLast().getLeftPart()), list1.getLast().getRightPart()));
        ans.addAll(generateAnotA(list1.getLast().getLeftPart()));
        ans.addLast(list1.getLast().getRightPart());
        return ans;
    }

    private static LinkedList<Expression> generateAnotA(Expression expr) {
        LinkedList<Expression> ans = new LinkedList<>();
        ans.addLast(new Implication(expr, new Or(expr, new Not(expr))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))))));
        ans.addLast(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr)))))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr))))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(expr, new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr)))), new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(expr, new Not(new Or(expr, new Not(expr)))), new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))), new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)))));
        ans.addLast(new Implication(new Implication(expr, new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr))));
        ans.addLast(new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)));
        ans.addLast(new Implication(new Not(expr), new Or(expr, new Not(expr))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Not(new Or(expr, new Not(expr))))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr))))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Not(expr), new Or(expr, new Not(expr)))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr))))), new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Implication(new Implication(new Not(expr), new Not(new Or(expr, new Not(expr)))), new Not(new Not(expr)))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))), new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))))));
        ans.addLast(new Implication(new Implication(new Not(expr), new Or(expr, new Not(expr))), new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr)))));
        ans.addLast(new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Not(expr)), new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))), new Not(new Not(new Or(expr, new Not(expr)))))));
        ans.addLast(new Implication(new Implication(new Not(new Or(expr, new Not(expr))), new Not(new Not(expr))), new Not(new Not(new Or(expr, new Not(expr))))));
        ans.addLast(new Not(new Not(new Or(expr, new Not(expr)))));
        ans.addLast(new Implication(new Not(new Not(new Or(expr, new Not(expr)))), new Or(expr, new Not(expr))));
        ans.addLast(new Or(expr, new Not(expr)));
        return ans;
    }

    private static LinkedList<Expression> deductProof(LinkedList<Expression> proof, Expression var, LinkedList<Expression> vars) {
        LinkedList<Expression> ans = new LinkedList<>();
        allImpl = new HashSet<>();
        modusPonensRightPart = new HashMap<>();
        for (Expression expr : proof) {
            if (isAxiom(expr)) {
                ans.addLast(new Implication(expr, new Implication(var, expr)));
                ans.addLast(expr);
                ans.addLast(new Implication(var, expr));
                addString(expr);
                continue;
            }
            if (expr.equals(var)) {
                ans.addLast(new Implication(expr, new Implication(expr, expr)));
                ans.addLast(new Implication(new Implication(expr, new Implication(expr, expr)), new Implication(new Implication(expr, new Implication(new Implication(expr, expr), expr)), new Implication(expr, expr))));
                ans.addLast(new Implication(expr, new Implication(new Implication(expr, expr), expr)));
                ans.addLast(new Implication(new Implication(expr, new Implication(new Implication(expr, expr), expr)), new Implication(expr, expr)));
                ans.addLast(new Implication(expr, expr));
                addString(expr);
                continue;
            }
            if (vars.contains(expr)) {
                ans.addLast(new Implication(expr, new Implication(var, expr)));
                ans.addLast(expr);
                ans.addLast(new Implication(var, expr));
                addString(expr);
                continue;
            }
            for (Expression k : modusPonensRightPart.get(expr)) {
                if (allImpl.contains(k)) {
                    ans.addLast(new Implication(new Implication(var, k), new Implication(new Implication(var, new Implication(k, expr)), new Implication(var, expr))));
                    ans.addLast(new Implication(new Implication(var, new Implication(k, expr)), new Implication(var, expr)));
                    ans.addLast(new Implication(var, expr));
                    addString(expr);
                    break;
                }
            }
        }
        return ans;
    }

    private static void addString(Expression expr) {
        allImpl.add(expr);
        if (expr.getType() == 1) {
            modusPonensRightPart.putIfAbsent(expr.getRightPart(), new LinkedList<>());
            modusPonensRightPart.get(expr.getRightPart()).addFirst(expr.getLeftPart());
        }
    }

    private static boolean isAxiom(Expression expr) {
        return getNumberAxiom(expr) != 0;
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
        return 0;
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

    private static void proof(Expression expr, LinkedList<Expression> ans) {
        if (expr.getType() == 2) {
            proof(expr.getLeftPart(), ans);
            proof(expr.getRightPart(), ans);
            ans.addLast(new Implication(expr.getLeftPart(), new Implication(expr.getRightPart(), expr)));
            ans.addLast(new Implication(expr.getRightPart(), expr));
            ans.addLast(expr);
        } else {
            if (expr.getType() == 3) {
                if (expr.getLeftPart().eval() == 1) {
                    proof(expr.getLeftPart(), ans);
                    ans.addLast(new Implication(expr.getLeftPart(), expr));
                    ans.addLast(expr);
                } else {
                    if (expr.getRightPart().eval() == 1) {
                        proof(expr.getRightPart(), ans);
                        ans.addLast(new Implication(expr.getRightPart(), expr));
                        ans.addLast(expr);
                    }
                }
            } else {
                if (expr.getType() == 1) {
                    if (expr.getRightPart().eval() == 1) {
                        proof(expr.getRightPart(), ans);
                        ans.addLast(new Implication(expr.getRightPart(), expr));
                        ans.addLast(expr);
                    } else {
                        if (expr.getLeftPart().eval() == 0) {
                            proof(new Not(expr.getRightPart()), ans);
                            proof(new Not(expr.getLeftPart()), ans);
                            ans.addLast(new Implication(new Not(expr.getLeftPart()), new Implication(expr.getLeftPart(), new Not(expr.getLeftPart()))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Not(expr.getLeftPart())));
                            ans.addLast(new Implication(new Not(expr.getRightPart()), new Implication(expr.getLeftPart(), new Not(expr.getRightPart()))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Not(expr.getRightPart())));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), expr.getLeftPart())));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Implication(expr.getLeftPart(), expr.getLeftPart()), expr.getLeftPart())));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), expr.getLeftPart())), new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(expr.getLeftPart(), expr.getLeftPart()), expr.getLeftPart())), new Implication(expr.getLeftPart(), expr.getLeftPart()))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(expr.getLeftPart(), expr.getLeftPart()), expr.getLeftPart())), new Implication(expr.getLeftPart(), expr.getLeftPart())));
                            ans.addLast(new Implication(expr.getLeftPart(), expr.getLeftPart()));
                            ans.addLast(new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))));
                            ans.addLast(new Implication(new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))), new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart()))))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())), new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart()))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), expr.getLeftPart()), new Implication(new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart()))), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart()))), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart()))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())));
                            ans.addLast(new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))));
                            ans.addLast(new Implication(new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Not(expr.getLeftPart())), new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())))), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getLeftPart()), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())))), new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), expr.getLeftPart())), new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart()))))), new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), expr.getLeftPart()), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart()))))), new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart()))))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart()))), new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))), new Implication(expr.getLeftPart(), new Not(new Not(expr.getRightPart()))))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Implication(new Not(expr.getRightPart()), new Not(expr.getLeftPart())), new Not(new Not(expr.getRightPart())))), new Implication(expr.getLeftPart(), new Not(new Not(expr.getRightPart())))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Not(new Not(expr.getRightPart()))));
                            ans.addLast(new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart()));
                            ans.addLast(new Implication(new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart()), new Implication(expr.getLeftPart(), new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart()))));
                            ans.addLast(new Implication(expr.getLeftPart(), new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart())));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Not(new Not(expr.getRightPart()))), new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart())), new Implication(expr.getLeftPart(), expr.getRightPart()))));
                            ans.addLast(new Implication(new Implication(expr.getLeftPart(), new Implication(new Not(new Not(expr.getRightPart())), expr.getRightPart())), new Implication(expr.getLeftPart(), expr.getRightPart())));
                            ans.addLast(expr);
                        }
                    }
                } else {
                    if (expr.getType() == 4) {
                        if (expr.getRightPart().getType() == 4) {
                            proof(expr.getRightPart().getRightPart(), ans);
                            ans.addLast(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), expr.getRightPart().getRightPart())));
                            ans.addLast(new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), expr.getRightPart().getRightPart()));
                            ans.addLast(new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), new Not(expr.getRightPart().getRightPart())));
                            ans.addLast(new Implication(new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), new Not(expr.getRightPart().getRightPart())), new Not(new Not(new Not(new Not(expr.getRightPart().getRightPart())))))));
                            ans.addLast(new Implication(new Implication(new Not(new Not(new Not(expr.getRightPart().getRightPart()))), new Not(expr.getRightPart().getRightPart())), new Not(new Not(new Not(new Not(expr.getRightPart().getRightPart()))))));
                            ans.addLast(new Not(new Not(new Not(new Not(expr.getRightPart().getRightPart())))));
                            ans.addLast(new Implication(new Not(new Not(new Not(new Not(expr.getRightPart().getRightPart())))), new Not(new Not(expr.getRightPart().getRightPart()))));
                            ans.addLast(expr);
                        } else {
                            if (expr.getRightPart().getType() == 1) {
                                proof(expr.getRightPart().getLeftPart(), ans);
                                proof(new Not(expr.getRightPart().getRightPart()), ans);
                                ans.addLast(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart()))));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())));
                                ans.addLast(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart()))));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart())));
                                ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart()));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                ans.addLast(expr);
                            } else {
                                if (expr.getRightPart().getType() == 3) {
                                    proof(new Not(expr.getRightPart().getLeftPart()), ans);
                                    proof(new Not(expr.getRightPart().getRightPart()), ans);
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())), new Not(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Not(expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())));
                                    ans.addLast(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))));
                                    ans.addLast(new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart())), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Implication(new Not(expr.getRightPart().getLeftPart()), new Not(expr.getRightPart().getRightPart())), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()));
                                    ans.addLast(new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart())))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Not(new Not(expr.getRightPart().getLeftPart()))), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart()))), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), new Implication(new Not(new Not(expr.getRightPart().getLeftPart())), expr.getRightPart().getLeftPart())), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getLeftPart(), expr.getRightPart().getLeftPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart())), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Implication(expr.getRightPart().getRightPart(), expr.getRightPart().getLeftPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())), new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()))));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())), new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart())));
                                    ans.addLast(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()));
                                    ans.addLast(new Implication(new Implication(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())), new Not(new Or(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                    ans.addLast(expr);
                                } else {
                                    if (expr.getRightPart().getType() == 2) {
                                        if (expr.getRightPart().getLeftPart().eval() == 0) {
                                            proof(new Not(expr.getRightPart().getLeftPart()), ans);
                                            ans.addLast(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()));
                                            ans.addLast(new Implication(new Not(expr.getRightPart().getLeftPart()), new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart()))));
                                            ans.addLast(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())));
                                            ans.addLast(new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getLeftPart()), new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())), new Not(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                            ans.addLast(new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getLeftPart())), new Not(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                            ans.addLast(expr);
                                        } else {
                                            if (expr.getLeftPart().getRightPart().eval() == 0) {
                                                proof(new Not(expr.getRightPart().getRightPart()), ans);
                                                ans.addLast(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart()));
                                                ans.addLast(new Implication(new Not(expr.getRightPart().getRightPart()), new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart()))));
                                                ans.addLast(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())));
                                                ans.addLast(new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), expr.getRightPart().getRightPart()), new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())), new Not(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart())))));
                                                ans.addLast(new Implication(new Implication(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()), new Not(expr.getRightPart().getRightPart())), new Not(new And(expr.getRightPart().getLeftPart(), expr.getRightPart().getRightPart()))));
                                                ans.addLast(expr);
                                            }
                                        }
                                    } else {
                                        ans.addLast(expr);
                                    }
                                }
                            }
                        }
                    } else {
                        ans.addLast(expr);
                    }
                }
            }
        }
    }

    private static boolean findHypo(int[] a) {
        boolean flag = false;
        for (int value : a) {
            if (value != 1) {
                flag = true;
                break;
            }
        }
        if (flag) {
            switch (consts.size()) {
                case 1:
                    if (a[1] == 1) {
                        hypo.add(consts.get(0));
                        return true;
                    }
                    break;
                case 2: {
                    if (find(1, 2, a)) {
                        hypo.add(consts.get(0));
                        return true;
                    }
                    if (find(2, 2, a)) {
                        hypo.add(consts.get(1));
                        return true;
                    }
                    if (a[3] == 1) {
                        hypo.add(consts.get(0));
                        hypo.add(consts.get(1));
                        return true;
                    }
                    break;
                }
                case 3: {
                    if (find(1, 3, a)) {
                        hypo.add(consts.get(0));
                        return true;
                    }
                    if (find(2, 3, a)) {
                        hypo.add(consts.get(1));
                        return true;
                    }
                    if (find(4, 3, a)) {
                        hypo.add(consts.get(2));
                        return true;
                    }
                    if (find(5, 3, a)) {
                        hypo.add(consts.get(0));
                        hypo.add(consts.get(2));
                        return true;
                    }
                    if (find(3, 3, a)) {
                        hypo.add(consts.get(0));
                        hypo.add(consts.get(1));
                        return true;
                    }
                    if (find(6, 3, a)) {
                        hypo.add(consts.get(1));
                        hypo.add(consts.get(2));
                        return true;
                    }
                    if (a[7] == 1) {
                        hypo.add(consts.get(0));
                        hypo.add(consts.get(1));
                        hypo.add(consts.get(2));
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private static String exp;
    private static int ind;

    private static boolean check(int a, int b) {
        return a == b && a == 1;
    }

    private static boolean find(int t, int k, int[] a) {
        boolean flag = true;
        for (int i = 0; i < (1 << k); i++) {
            for (int j = 0; j < (1 << k); j++) {
                if (i != j) {
                    if ((i & j) == t) {
                        flag = flag && check(a[i], a[j]);
                    }
                }
            }
        }
        return flag;
    }

    public static Expression parse(String expression) {
        exp = normalise(expression);
        ind = 0;
        return parseImpl();
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
        if (chr == '(') {
            ind++;
            Expression expr = parseImpl();
            ind++;
            return expr;
        } else {
            StringBuilder str = new StringBuilder();
            while (ind < exp.length() && (Character.isLetter(exp.charAt(ind)) || Character.isDigit(exp.charAt(ind)) || exp.charAt(ind) == '\'')) {
                str.append(exp.charAt(ind));
                ind++;
            }
            Expression con = new Const(str.toString());
            for (Expression co : consts) {
                if (co.equals(con)) {
                    return co;
                }
            }
            consts.add(con);
            return con;
        }
    }

    public static String normalise(String s) {
        StringBuilder str = new StringBuilder();
        String[] split = s.split("\\s+");
        for (String st : split) {
            str.append(st);
        }
        return str.toString();
    }

}