import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class F {
    private static ArrayList<Pair>[] list;
    private static PriorityQueue<Pair> queue;

    public static class Pair implements Comparable<Pair> {
        int number;
        long weight;

        Pair(int n, long w) {
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
        int a, b, c;
        list = new ArrayList[n];
        double[] d = new double[n];
        boolean[] used = new boolean[n];
        queue = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            d[i] = Double.POSITIVE_INFINITY;
            list[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            long weight = Long.parseLong(split[2]);
            list[from].add(new Pair(to, weight));
            list[to].add(new Pair(from, weight));
        }
        split = in.readLine().split("[\\s]");
        a = Integer.parseInt(split[0]) - 1;
        b = Integer.parseInt(split[1]) - 1;
        c = Integer.parseInt(split[2]) - 1;
        queue.add(new Pair(a, 0));
        for (int i = 0; i < n; i++) {
            int minV = -1;
            long minW = -1;
            while (!queue.isEmpty()) {
                if (!used[queue.peek().number]) {
                    minV = queue.peek().number;
                    minW = queue.peek().weight;
                    d[minV] = minW;
                    used[minV] = true;
                    queue.remove();
                    break;
                } else {
                    queue.remove();
                }
            }
            if (minV == -1) {
                break;
            }
            for (int k = 0; k < list[minV].size(); k++) {
                int u = list[minV].get(k).number;
                long w = list[minV].get(k).weight;
                queue.add(new Pair(u, minW + w));
            }
        }
        double s1, s2, s3;
        s1 = d[b];
        s2 = d[c];
        d = new double[n];
        used = new boolean[n];
        queue = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            d[i] = Double.POSITIVE_INFINITY;
        }
        queue.add(new Pair(b, 0));
        for (int i = 0; i < n; i++) {
            int minV = -1;
            long minW = -1;
            while (!queue.isEmpty()) {
                if (!used[queue.peek().number]) {
                    minV = queue.peek().number;
                    minW = queue.peek().weight;
                    d[minV] = minW;
                    used[minV] = true;
                    queue.remove();
                    break;
                } else {
                    queue.remove();
                }
            }
            if (minV == -1) {
                break;
            }
            for (int k = 0; k < list[minV].size(); k++) {
                int u = list[minV].get(k).number;
                long w = list[minV].get(k).weight;
                queue.add(new Pair(u, minW + w));
            }
        }
        s3 = d[c];
        double ans = Math.min(s1 + s2, Math.min(s2 + s3, s1 + s3));
        if (ans == Double.POSITIVE_INFINITY) {
            System.out.println(-1);
        } else {
            System.out.println((long) ans);
        }
    }
}