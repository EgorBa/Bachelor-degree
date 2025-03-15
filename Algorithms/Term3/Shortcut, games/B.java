import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class B {
    private static ArrayList<Pair>[] list;
    private static PriorityQueue<Pair> queue;

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
        list = new ArrayList[n];
        long[] d = new long[n];
        boolean[] used = new boolean[n];
        queue = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            d[i] = Integer.MAX_VALUE;
            list[i] = new ArrayList();
        }
        queue.add(new Pair(0, 0));
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int weight = Integer.parseInt(split[2]);
            list[from].add(new Pair(to, weight));
            list[to].add(new Pair(from, weight));
        }
        for (int i = 0; i < n; i++) {
            int minV = -1;
            int minW = -1;
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
                int w = list[minV].get(k).weight;
                queue.add(new Pair(u, minW + w));
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.print(d[i] + " ");
        }
    }
}
