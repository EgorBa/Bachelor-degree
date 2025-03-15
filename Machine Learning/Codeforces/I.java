import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class I {

    //1 - var, 2 - tnh, 3 - rlu, 4 - mul, 5 - sum, 6 - had

    public static class Node {
        int type;
        double alpha = 0;
        boolean counted = false;
        boolean diffed = false;
        double[][] matrix;
        double[][] diff;
        ArrayList<Node> straight = new ArrayList<>();
        ArrayList<Node> reverse = new ArrayList<>();

        Node(int r, int c, int t) {
            if (t == 1) {
                counted = true;
            }
            matrix = new double[r][c];
            type = t;
        }

        Node(int t) {
            type = t;
        }

        Node(int t, int alpha) {
            type = t;
            this.alpha = alpha;
        }

        public void printMatrix() throws IOException {
            for (double[] doubles : matrix) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (Double.isNaN(doubles[j])){
                        throw new IOException();
                    }
                    System.out.print(doubles[j] + " ");
                }
                System.out.println();
            }
        }

        public void printDiff() throws IOException {
            for (double[] doubles : diff) {
                for (int j = 0; j < diff[0].length; j++) {
                    if (Double.isNaN(doubles[j])){
                        throw new IOException();
                    }
                    System.out.print(doubles[j] + " ");
                }
                System.out.println();
            }
        }

        public void count() {
            switch (type) {
                case 2: {
                    Node node = reverse.get(0);
                    int r = node.matrix.length;
                    int c = node.matrix[0].length;
                    if (!counted) {
                        matrix = new double[r][c];
                    }
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            matrix[i][j] += Math.tanh(node.matrix[i][j]);
                        }
                    }
                    break;
                }
                case 3: {
                    Node node = reverse.get(0);
                    int r = node.matrix.length;
                    int c = node.matrix[0].length;
                    if (!counted) {
                        matrix = new double[r][c];
                    }
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            if (node.matrix[i][j] > 0) {
                                matrix[i][j] += node.matrix[i][j];
                            } else {
                                matrix[i][j] += node.matrix[i][j] / alpha;
                            }
                        }
                    }
                    break;
                }
                case 4: {
                    Node node1 = reverse.get(0);
                    Node node2 = reverse.get(1);
                    int r = node1.matrix.length;
                    int c = node2.matrix[0].length;
                    int cr = node1.matrix[0].length;
                    if (!counted) {
                        matrix = new double[r][c];
                    }
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            for (int k = 0; k < cr; k++) {
                                matrix[i][j] += node1.matrix[i][k] * node2.matrix[k][j];
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    Node node = reverse.get(0);
                    int r = node.matrix.length;
                    int c = node.matrix[0].length;
                    if (!counted) {
                        matrix = new double[r][c];
                    }
                    for (Node n : reverse) {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                matrix[i][j] += n.matrix[i][j];
                            }
                        }
                    }
                    break;
                }
                case 6: {
                    Node node = reverse.get(0);
                    int r = node.matrix.length;
                    int c = node.matrix[0].length;
                    if (!counted) {
                        matrix = new double[r][c];
                    }
                    double[][] buf = new double[r][c];
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            buf[i][j] = 1;
                        }
                    }
                    for (Node n : reverse) {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                buf[i][j] *= n.matrix[i][j];
                            }
                        }
                    }
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            matrix[i][j] += buf[i][j];
                        }
                    }
                    break;
                }
            }
            counted = true;
        }

        public void diff() {
            int r = matrix.length;
            int c = matrix[0].length;
            if (!diffed) {
                diff = new double[r][c];
            }
            diffed = true;
            for (Node n : straight) {
                switch (n.type) {
                    case 2: {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                diff[i][j] += n.diff[i][j] / (Math.cosh(matrix[i][j]) * Math.cosh(matrix[i][j]));
                            }
                        }
                        break;
                    }
                    case 3: {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                if (matrix[i][j] < 0) {
                                    diff[i][j] += n.diff[i][j] / n.alpha;
                                } else {
                                    diff[i][j] += n.diff[i][j];
                                }
                            }
                        }
                        break;
                    }
                    case 4: {
                        if (n.reverse.get(0) == this) {
                            for (int i = 0; i < r; i++) {
                                for (int j = 0; j < c; j++) {
                                    for (int k = 0; k < n.diff[0].length; k++) {
                                        diff[i][j] += n.diff[i][k] * n.reverse.get(1).matrix[j][k];
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < r; i++) {
                                for (int j = 0; j < c; j++) {
                                    for (int k = 0; k < n.diff.length; k++) {
                                        diff[i][j] += n.reverse.get(0).matrix[k][i] * n.diff[k][j];
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case 5: {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                diff[i][j] += n.diff[i][j];
                            }
                        }
                        break;
                    }
                    case 6: {
                        for (int i = 0; i < r; i++) {
                            for (int j = 0; j < c; j++) {
                                double mul = 1;
                                for (Node n1 : n.reverse) {
                                    if (n1 == this) continue;
                                    mul *= n1.matrix[i][j];
                                }
                                diff[i][j] += mul * n.diff[i][j];
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("\\s");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int k = Integer.parseInt(split[2]);
        ArrayList<Node> startNodes = new ArrayList<>();
        ArrayList<Node> endNodes = new ArrayList<>();
        ArrayList<Node> all = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("\\s");
            Node node = null;
            int number;
            int length;
            switch (split[0]) {
                case "var": {
                    node = new Node(Integer.parseInt(split[1]), Integer.parseInt(split[2]), 1);
                    break;
                }
                case "tnh": {
                    node = new Node(2);
                    number = Integer.parseInt(split[1]) - 1;
                    all.get(number).straight.add(node);
                    node.reverse.add(all.get(number));
                    break;
                }
                case "rlu": {
                    node = new Node(3, Integer.parseInt(split[1]));
                    number = Integer.parseInt(split[2]) - 1;
                    all.get(number).straight.add(node);
                    node.reverse.add(all.get(number));
                    break;
                }
                case "mul": {
                    node = new Node(4);
                    number = Integer.parseInt(split[1]) - 1;
                    all.get(number).straight.add(node);
                    node.reverse.add(all.get(number));
                    number = Integer.parseInt(split[2]) - 1;
                    all.get(number).straight.add(node);
                    node.reverse.add(all.get(number));
                    break;
                }
                case "sum": {
                    node = new Node(5);
                    length = Integer.parseInt(split[1]);
                    for (int j = 0; j < length; j++) {
                        number = Integer.parseInt(split[2 + j]) - 1;
                        all.get(number).straight.add(node);
                        node.reverse.add(all.get(number));
                    }
                    break;
                }
                case "had": {
                    node = new Node(6);
                    length = Integer.parseInt(split[1]);
                    for (int j = 0; j < length; j++) {
                        number = Integer.parseInt(split[2 + j]) - 1;
                        all.get(number).straight.add(node);
                        node.reverse.add(all.get(number));
                    }
                    break;
                }
            }
            if (i >= n - k) {
                endNodes.add(node);
            }
            if (i < m) {
                startNodes.add(node);
            }
            all.add(node);
        }
        for (Node node : startNodes) {
            for (int i = 0; i < node.matrix.length; i++) {
                split = in.readLine().split("\\s");
                for (int j = 0; j < split.length; j++) {
                    node.matrix[i][j] = Integer.parseInt(split[j]);
                }
            }
        }
        count(all);
        for (Node node : endNodes) {
            int r = node.matrix.length;
            int c = node.matrix[0].length;
            node.diffed = true;
            node.diff = new double[r][c];
            for (int i = 0; i < r; i++) {
                split = in.readLine().split("\\s");
                for (int j = 0; j < split.length; j++) {
                    node.diff[i][j] = Integer.parseInt(split[j]);
                }
            }
        }
        countDiff(all);
        for (Node node : endNodes) {
            node.printMatrix();
        }
        for (Node node : startNodes) {
            node.printDiff();
        }
    }

    public static void count(ArrayList<Node> all) {
        LinkedList<Node> nodes = new LinkedList<>(all);
        while (!nodes.isEmpty()) {
            Node node = nodes.getFirst();
            nodes.removeFirst();
            if (!node.counted) {
                node.count();
            }
        }
    }

    public static void countDiff(ArrayList<Node> all) {
        LinkedList<Node> nodes = new LinkedList<>(all);
        while (!nodes.isEmpty()) {
            Node node = nodes.getLast();
            nodes.removeLast();
            node.diff();
        }
    }
}
