import java.io.*;
import java.util.*;

public class G {
    private static String[] strs;
    private static int n;
    private static String str;
    private static Node root;
    private static boolean[] term;
    private static HashMap<String, HashSet<Integer>> map = new HashMap<>();

    private static class Node {
        HashMap<Character, Node> next;
        Node suf;
        Node p;
        boolean term;
        boolean flag;
        HashSet<Integer> set_term;
        char chr_in;

        Node(Node suf, Node p, char chr) {
            chr_in = chr;
            this.p = p;
            this.suf = suf;
            next = new HashMap<>();
            term = false;
            flag = false;
        }

        boolean contains(char chr) {
            return next.containsKey(chr);
        }

        Node get(char chr) {
            return next.get(chr);
        }

        void add(char chr, Node node) {
            next.put(chr, new Node(null, node, chr));
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search4.in"));
        n = Integer.parseInt(in.readLine());
        strs = new String[n];
        term = new boolean[n];
        for (int i = 0; i < n; i++) {
            strs[i] = in.readLine();
            term[i] = false;
        }
        str = in.readLine();
        root = new Node(null, null, ' ');
        for (int i = 0; i < n; i++) {
            Node node = root;
            for (int j = 0; j < strs[i].length(); j++) {
                if (!node.contains(strs[i].charAt(j))) {
                    node.add(strs[i].charAt(j), node);
                }
                node = node.get(strs[i].charAt(j));
                if (j == strs[i].length() - 1) {
                    if (node.set_term == null) {
                        node.set_term = new HashSet<>();
                    }
                    node.set_term.add(i);
                    node.term = true;
                }
            }
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new FileWriter("search4.out"));
        Node node = root;
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (node.contains(chr)) {
                node = node.get(chr);
            } else {
                while (node != root && !node.contains(chr)) {
                    node = get_link(node);
                }
                if (node.contains(chr)) {
                    node = node.get(chr);
                } else {
                    node = root;
                }
            }
            term_set_make(node);
        }
        for (int i = 0; i < n; i++) {
            out.write((term[i] ? "YES" : "NO") + '\n');
        }
        out.close();
    }

    private static void print(Node p) {
        System.out.println(get_link(p).next);
        for (char chr : p.next.keySet()) {
            print(p.get(chr));
        }
    }

    private static HashSet<Integer> term_set_make(Node p) {
        if (!p.flag) {
            if (p == root) {
                p.flag = true;
                p.set_term = new HashSet<>();
                return new HashSet<>();
            } else {
                if (p.set_term == null) {
                    p.set_term = new HashSet<>();
                }
                p.set_term.addAll(term_set_make(get_link(p)));
                p.flag = true;
                for (int j : p.set_term) {
                    term[j] = true;
                }
                return p.set_term;
            }
        } else {
            return new HashSet<>();
        }
    }

    private static Node get_link(Node node) {
        if (node.suf != null) {
            return node.suf;
        } else {
            Node p = node.p;
            char chr = node.chr_in;
            while (true) {
                if (node == root || p == root) {
                    node.suf = root;
                    return root;
                } else {
                    p = get_link(p);
                    if (p.contains(chr)) {
                        node.suf = p.get(chr);
                        return node.suf;
                    }
                }
            }
        }
    }
}