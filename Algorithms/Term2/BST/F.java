import java.io.*;
import java.util.Random;

public class F {

    public static class Node {
        Node left;
        Node right;
        int priority;
        long key;
        long sum;

        private Node(long key, int priority) {
            this.key = key;
            this.priority = priority;
            this.sum = 1;
        }
    }

    public static class Nodes {
        Node left;
        Node right;

        private Nodes(Node left, Node right) {
            this.left = left;
            this.right = right;
        }
    }

    private static Node root = null;

    private static Node merge(Node l, Node r) {
        if (r == null) {
            return l;
        } else {
            if (l == null) {
                return r;
            } else {
                if (l.priority < r.priority) {
                    r.left = merge(l, r.left);
                    r.sum = trueSum(r.left) + trueSum(r.right) + 1;
                    return r;
                } else {
                    l.right = merge(l.right, r);
                    l.sum = trueSum(l.left) + trueSum(l.right) + 1;
                    return l;
                }
            }
        }
    }

    private static void insert(long key, int priority) {
        Node v = root;
        while ((v != null) && (v.key != key)) {
            if (key < v.key) {
                v = v.left;
            } else {
                v = v.right;
            }
        }
        Node v1 = new Node(key, priority);
        Nodes nodes = split(root, key, priority);
        root = merge(nodes.left, merge(v1, nodes.right));
    }

    private static Nodes split(Node v, long key, int priority) {
        if (v == null) {
            return new Nodes(null, null);
        } else {
            if (v.key < key) {
                Nodes nodes = split(v.right, key, priority);
                v.right = null;
                v.sum = trueSum(v.left) + 1;
                return new Nodes(merge(v, nodes.left), nodes.right);
            } else {
                Nodes nodes = split(v.left, key, priority);
                v.left = null;
                v.sum = trueSum(v.right) + 1;
                return new Nodes(nodes.left, merge(nodes.right, v));
            }
        }
    }

    private static long trueSum(Node v) {
        if (v == null) {
            return 0;
        } else {
            return v.sum;
        }
    }

    private static Node delete(Node v, int key) {
        if (key < v.key) {
            v.left = delete(v.left, key);
        } else {
            if (key > v.key) {
                v.right = delete(v.right, key);
            } else {
                return merge(v.left, v.right);
            }
        }
        v.sum = trueSum(v.left) + trueSum(v.right) + 1;
        return v;
    }

    private static long find(long key, Node v) {
        if (key - 1 == trueSum(v.left)) {
            return v.key;
        } else {
            if (trueSum(v.left) > key - 1) {
                return find(key, v.left);
            } else {
                return find(key - trueSum(v.left) - 1, v.right);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        int y = 0;
        for (int i = 0; i < n; i++) {
            String[] split = reader.readLine().split("[\\s]");
            if (split[0].equals("+1") || split[0].equals("1")) {
                insert(Integer.parseInt(split[1]), new Random().nextInt());
                y++;
            } else {
                if (split[0].equals("-1")) {
                    root = delete(root, Integer.parseInt(split[1]));
                    y--;
                } else {
                    System.out.println(find(y - Integer.parseInt(split[1]) + 1, root));
                }
            }
        }
    }
}