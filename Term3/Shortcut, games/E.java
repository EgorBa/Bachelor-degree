import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class E {
    private static ArrayList<Pair>[] list;
    private static boolean[] used;
    private static double[] d1;

    public static class Pair implements Comparable<Pair> {
        int number;
        double weight;

        Pair(int n, double w) {
            number = n;
            weight = w;
        }

        @Override
        public int compareTo(Pair o) {
            if (o.weight == weight) return 0;
            return o.weight < weight ? 1 : -1;
        }

        public String toString() {
            return number + " " + weight;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int s = Integer.parseInt(split[2]) - 1;
        list = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            list[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            double weight = Double.parseDouble(split[2]);
            list[from].add(new Pair(to, weight));
        }
        double[] d = new double[n];
        d1 = new double[n];
        used = new boolean[n];
        for (int i = 0; i < n; i++) {
            d[i] = Double.POSITIVE_INFINITY;
            d1[i] = Double.POSITIVE_INFINITY;
        }
        d[s] = 0;
        d1[s] = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int y = 0; y < list[j].size(); y++) {
                    int u = list[j].get(y).number;
                    double w = list[j].get(y).weight;
                    if (d[u] > d[j] + w) {
                        d[u] = d[j] + w;
                    }
                    if (d1[u] > d1[j] + w) {
                        d1[u] = d1[j] + w;
                    }
                }
            }
        }
        for (int j = 0; j < n; j++) {
            for (int y = 0; y < list[j].size(); y++) {
                int u = list[j].get(y).number;
                double w = list[j].get(y).weight;
                if (d1[u] > d1[j] + w) {
                    d1[u] = d1[j] + w;
                }
            }
        }
        ArrayList<Integer> top = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (d1[i] < d[i]) {
                top.add(i);
            }
        }
        for (int i = 0; i < top.size(); i++) {
            dfs(top.get(i));
        }
        for (int i = 0; i < n; i++) {
            if (d1[i] < d[i]) {
                System.out.println("-");
                continue;
            }
            if (d[i] == Double.POSITIVE_INFINITY) {
                System.out.println("*");
                continue;
            }
            System.out.println((long) d[i]);
        }
    }

    private static void dfs(int u) {
        used[u] = true;
        for (int i = 0; i < list[u].size(); i++) {
            int v = list[u].get(i).number;
            if (!used[v]) {
                d1[v] = Double.NEGATIVE_INFINITY;
                dfs(v);
            }
        }
    }
}