import java.io.*;
import java.util.*;

public class I {
    private static String[] strs;
    private static int n;
    private static String str;
    private static Node root;
    private static int[][] term_count;
    private static LinkedList<Node> lol;

    private static class Int {
        Int i;
        int count;
        int first, last;
        LinkedList<Integer> list;

        Int() {
            i = null;
            count = 0;
            list = new LinkedList<>();
            first = -1;
            last = -1;
        }
    }

    private static class Node {
        HashMap<Character, Node> next;
        int first, last;
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
            first = -1;
            last = -1;
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
        BufferedReader in = new BufferedReader(new FileReader("search6.in"));
        n = Integer.parseInt(in.readLine());
        strs = new String[n];
        term_count = new int[2][n];
        LinkedList<Int> kek = new LinkedList<>();
        lol = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            strs[i] = in.readLine();
            term_count[0][i] = 0;
            term_count[1][i] = 0;
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
        BufferedWriter out = new BufferedWriter(new FileWriter("search6.out"));
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
            if (node.first == -1) {
                node.first = i;
            }
            node.last = i;
            node.count++;
        }
        for (Node n : lol) {
            int first = n.first;
            int last = n.last;
            int i = n.count;
            Int buf = term_set_make(n);
            while (buf != null) {
                buf.count += i;
                if (first != -1) {
                    if (buf.first != -1) {
                        buf.first = Math.min(first, buf.first);
                        buf.last = Math.max(last, buf.last);
                    } else {
                        buf.first = first;
                        buf.last = last;
                    }
                }
                buf = buf.i;
            }
        }
        for (Int buf : kek) {
            for (int j : buf.list) {
                if (buf.count == 0) {
                    term_count[0][j] = term_count[1][j] = -1;
                } else {
                    term_count[0][j] = buf.first - strs[j].length() + 1;
                    term_count[1][j] = buf.last - strs[j].length() + 1;
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append(term_count[0][i]).append(" ").append(term_count[1][i]);
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