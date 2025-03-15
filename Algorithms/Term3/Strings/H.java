import java.io.*;
import java.util.*;

public class H {
    private static String[] strs;
    private static int n;
    private static String str;
    private static Node root;
    private static int[] term_count;
    private static LinkedList<Node> lol;

    private static class Int {
        Int i;
        int count;
        LinkedList<Integer> list;

        Int() {
            i = null;
            count = 0;
            list = new LinkedList<>();
        }
    }

    private static class Node {
        HashMap<Character, Node> next;
        Node suf;
        Node p;
        int count;
        boolean term;
        boolean flag;
        Int i;
        char chr_in;

        Node(Node suf, Node p, char chr) {
            chr_in = chr;
            this.p = p;
            this.suf = suf;
            count = 0;
            next = new HashMap<>();
            term = false;
            flag = false;
            Int i = null;
        }

        boolean contains(char chr) {
            return next.containsKey(chr);
        }

        Node get(char chr) {
            return next.get(chr);
        }

        void add(char chr, Node node) {
            next.put(chr, new Node(null, node, chr));
            lol.addLast(next.get(chr));
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search5.in"));
        n = Integer.parseInt(in.readLine());
        strs = new String[n];
        term_count = new int[n];
        LinkedList<Int> kek = new LinkedList<>();
        lol = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            strs[i] = in.readLine();
            term_count[i] = 0;
        }
        str = in.readLine();
        root = new Node(null, null, ' ');
        for (int i = 0; i < n; i++) {
            if (strs[i].length() > str.length()) {
                continue;
            }
            Node node = root;
            for (int j = 0; j < strs[i].length(); j++) {
                if (!node.contains(strs[i].charAt(j))) {
                    node.add(strs[i].charAt(j), node);
                }
                node = node.get(strs[i].charAt(j));
                if (j == strs[i].length() - 1) {
                    if (node.i == null) {
                        node.i = new Int();
                        kek.addLast(node.i);
                        node.term = true;
                    }
                    node.i.list.add(i);
                }
            }
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new FileWriter("search5.out"));
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
            node.count++;
        }
        for (Node n : lol) {
            int i = n.count;
            Int buf = term_set_make(n);
            while (buf != null) {
                buf.count += i;
                buf = buf.i;
            }
        }
        for (Int buf : kek) {
            for (int j : buf.list) {
                term_count[j] = buf.count;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append(term_count[i]);
            stringBuilder.append('\n');
        }
        out.write(stringBuilder.toString());
        out.close();
    }

    private static void print(Node p) {
        System.out.println(get_link(p).next);
        for (char chr : p.next.keySet()) {
            print(p.get(chr));
        }
    }

    private static Int term_set_make(Node p) {
        if (!p.flag) {
            if (p == root) {
                p.flag = true;
                return null;
            } else {
                if (p.i == null) {
                    p.i = term_set_make(get_link(p));
                } else {
                    p.i.i = term_set_make(get_link(p));
                }
                p.flag = true;
                return p.i;
            }
        } else {
            return p.i;
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