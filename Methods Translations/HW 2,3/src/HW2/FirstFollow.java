package HW2;

import java.util.HashMap;
import java.util.HashSet;

public class FirstFollow {
    public static HashMap<Character, HashSet<Character>> buildFirst(HashSet<Character> nonTerm,
                                                                    HashSet<Character> term,
                                                                    HashSet<String> rules) {
        HashMap<Character, HashSet<Character>> first = new HashMap<>();
        for (char key : nonTerm) {
            first.putIfAbsent(key, new HashSet<>());
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String rule : rules) {
                char curChar = rule.charAt(0);
                char curNext = rule.charAt(2);
                if (term.contains(curNext)) {
                    changed = changed || first.get(curChar).add(curNext);
                } else {
                    changed = changed || first.get(curChar).addAll(first.get(curNext));
                }
            }
        }
        return first;
    }

    public static HashMap<Character, HashSet<Character>> buildFollow(HashSet<Character> nonTerm,
                                                                     HashSet<Character> term,
                                                                     HashSet<String> rules,
                                                                     char start,
                                                                     HashMap<Character, HashSet<Character>> first) {
        HashMap<Character, HashSet<Character>> follow = new HashMap<>();
        for (char key : nonTerm) {
            follow.putIfAbsent(key, new HashSet<>());
        }
        follow.get(start).add('$');
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String rule : rules) {
                for (int i = 2; i < rule.length(); i++) {
                    char curChar = rule.charAt(i);
                    if (term.contains(curChar)) {
                        continue;
                    }
                    if (i == rule.length() - 1) {
                        changed = changed || follow.get(curChar).addAll(follow.get(rule.charAt(0)));
                    } else {
                        char curNext = rule.charAt(i + 1);
                        if (term.contains(curNext)) {
                            changed = changed || follow.get(curChar).add(curNext);
                        } else {
                            int curSize = follow.get(curChar).size();
                            follow.get(curChar).addAll(first.get(curNext));
                            follow.get(curChar).remove('ε');
                            if (first.get(curNext).contains('ε')) {
                                follow.get(curChar).addAll(follow.get(rule.charAt(0)));
                            }
                            changed = changed || !(curSize == follow.get(curChar).size());
                        }
                    }
                }
            }
        }
        return follow;
    }

    public static void main(String[] args) {
        HashSet<Character> term = new HashSet<>();
        term.add(')');
        term.add('(');
        term.add('*');
        term.add('|');
        term.add('ε');
        term.add('t');
        HashSet<Character> nonTerm = new HashSet<>();
        nonTerm.add('A');
        nonTerm.add('O');
        nonTerm.add('B');
        nonTerm.add('P');
        nonTerm.add('C');
        nonTerm.add('T');
        nonTerm.add('K');
        HashSet<String> rules = new HashSet<>();
        rules.add("A-ε");
        rules.add("A-BO");
        rules.add("O-BO");
        rules.add("O-ε");
        rules.add("B-CP");
        rules.add("P-|CP");
        rules.add("P-ε");
        rules.add("C-TK");
        rules.add("K-*K");
        rules.add("K-ε");
        rules.add("T-(A)");
        rules.add("T-t");
        HashMap<Character, HashSet<Character>> first = buildFirst(nonTerm, term, rules);
        System.out.println("First");
        print(first);
        HashMap<Character, HashSet<Character>> follow = buildFollow(nonTerm, term, rules, 'A', first);
        System.out.println("Follow");
        print(follow);
    }

    private static void print(HashMap<Character, HashSet<Character>> first) {
        for (char key : first.keySet()) {
            System.out.print(key + " : ");
            for (char chr : first.get(key)) {
                System.out.print(chr + " ");
            }
            System.out.println();
        }
    }

}
