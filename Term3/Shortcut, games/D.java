import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class D {
    private static ArrayList<Pair>[] list;

    public static class Pair implements Comparable<Pair> {
        int number;
        int weight;

        Pair(int n, int w) {
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
        int k = Integer.parseInt(split[2]);
        int s = Integer.parseInt(split[3]) - 1;
        list = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            list[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int weight = Integer.parseInt(split[2]);
            list[from].add(new Pair(to, weight));
        }
        double[][] d = new double[n][101];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 101; j++) {
                d[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        d[s][0] = 0;
        for (int i = 1; i < 101; i++) {
            for (int j = 0; j < n; j++) {
                for (int y = 0; y < list[j].size(); y++) {
                    int u = list[j].get(y).number;
                    int w = list[j].get(y).weight;
                    if (d[u][i] > d[j][i - 1] + w) {
                        d[u][i] = d[j][i - 1] + w;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.println(d[i][k] == Double.POSITIVE_INFINITY ? -1 : (long) d[i][k]);
        }
    }
}