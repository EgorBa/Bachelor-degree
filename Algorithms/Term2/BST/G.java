import java.io.*;
import java.util.Random;

public class G {

    public static class Node {
        Node left;
        Node right;
        int priority;
        long key;
        long size;

        private Node(long key, int priority) {
            this.key = key;
            this.priority = priority;
            this.size = 1;
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
                    update(r);
                    return r;
                } else {
                    l.right = merge(l.right, r);
                    update(l);
                    return l;
                }
            }
        }
    }

    private static void update(Node t) {
        t.size = 1 + trueSize(t.left) + trueSize(t.right);
    }

    private static Nodes split(Node v, long key) {
        if (v == null) {
            return new Nodes(null, null);
        } else {
            long l = trueSize(v.left) + 1;
            if (l > key) {
                Nodes nodes = split(v.left, key);
                v.left = nodes.right;
                update(v);
                return new Nodes(nodes.left, v);
            } else {
                Nodes nodes = split(v.right, key - l);
                v.right = nodes.left;
                update(v);
                return new Nodes(v, nodes.right);
            }
        }
    }

    private static Node replace(int l, int r) {
        Nodes nodes = split(root, r);
        Nodes nodes1 = split(nodes.left, l);
        return merge(nodes1.right, merge(nodes1.left, nodes.right));
    }

    private static Node add(int pos, long key) {
        Nodes nodes = split(root, pos);
        Node m = new Node(key, new Random().nextInt());
        m = merge(merge(nodes.left, m), nodes.right);
        update(m);
        return m;
    }

    private static long trueSize(Node v) {
        if (v == null) {
            return 0;
        } else {
            return v.size;
        }
    }

    private static void print(Node v) {
        if (v != null) {
            print(v.left);
            System.out.print(v.key + " ");
            print(v.right);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] split = reader.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        for (int i = 1; i <= n; i++) {
            root = add(i - 1, i);
        }
        for (int i = 0; i < m; i++) {
            split = reader.readLine().split("[\\s]");
            root = replace(Integer.parseInt(split[0]) - 1, Integer.parseInt(split[1]));
        }
        print(root);
    }
}