import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class E {

    private static long sum;

    public static class Node {
        Node left;
        Node right;
        int priority;
        long key;
        long sum;

        private Node(long key, int priority) {
            this.key = key;
            this.priority = priority;
            this.sum = key;
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
                    r.sum = trueSum(r.left) + trueSum(r.right) + r.key;
                    return r;
                } else {
                    l.right = merge(l.right, r);
                    l.sum = trueSum(l.left) + trueSum(l.right) + l.key;
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
                v.sum = trueSum(v.left) + v.key;
                return new Nodes(merge(v, nodes.left), nodes.right);
            } else {
                Nodes nodes = split(v.left, key, priority);
                v.left = null;
                v.sum = trueSum(v.right) + v.key;
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

    private static long min(Node v) {
        if (v.left != null) {
            return min(v.left);
        } else {
            return v.key;
        }
    }

    private static long max(Node v) {
        if (v.right != null) {
            return max(v.right);
        } else {
            return v.key;
        }
    }

    private static void sum(int left, int right, Node v) {
        if (v != null) {
            if ((v.key >= left) && (v.key <= right)) {
                if ((min(v) >= left) && (max(v) <= right)) {
                    sum += v.sum;
                } else {
                    if (min(v) >= left) {
                        sum += v.key;
                        sum += trueSum(v.left);
                        sum(left, right, v.right);
                    } else {
                        if (max(v) <= right) {
                            sum += v.key;
                            sum += trueSum(v.right);
                            sum(left, right, v.left);
                        } else {
                            sum += v.key;
                            sum(left, right, v.left);
                            sum(left, right, v.right);
                        }
                    }
                }
            } else {
                if (v.key > right) {
                    sum(left, right, v.left);
                } else {
                    sum(left, right, v.right);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        HashMap<Long, Long> check = new HashMap<>();
        String[] split;
        long y = 0;
        String ask = "";
        for (int i = 0; i < n; i++) {
            split = reader.readLine().split("[\\s]");
            if (split[0].equals("+")) {
                long key;
                if ((i > 0) && (ask.equals("?"))) {
                    key = (Long.parseLong(split[1]) + y) % 1000000000;
                } else {
                    key = Long.parseLong(split[1]);
                }
                if (!check.containsKey(key)) {
                    check.put(key, key);
                    insert(key, new Random().nextInt());
                }
                ask = split[0];
            } else {
                sum = 0;
                sum(Integer.valueOf(split[1]), Integer.valueOf(split[2]), root);
                y = sum;
                System.out.println(y);
                ask = split[0];
            }
        }
    }
}