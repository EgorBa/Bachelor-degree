import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class K {
    private static int n = 0;
    private static int m = 0;
    private static HashSet<Integer>[] array;
    private static int[] g;
    private static boolean[] used;
    private static HashMap<Integer, Pair> map;

    public static class Pair implements Comparable<Pair> {
        int first;
        long second;

        Pair(int n, long w) {
            first = n;
            second = w;
        }

        @Override
        public int compareTo(Pair o) {
            if (o.second == second) return 0;
            return o.second < second ? 1 : -1;
        }

        public String toString() {
            return first + " " + second;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new HashSet[n];
        map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            array[i] = new HashSet<>();
        }
        for (int i = 0; i < n - 1; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            map.put(i, new Pair(parent, child));
            array[parent].add(child);
            array[child].add(parent);
        }
        g = new int[n];
        used = new boolean[n];
        dfs(m - 1);
        if (g[m - 1] == 0) {
            System.out.println("2");
        } else {
            System.out.println("1");
            used = new boolean[n];
            new_dfs(m - 1, 0);
        }
    }

    private static void dfs(int v) {
        used[v] = true;
        for (int u : array[v]) {
            array[u].remove(v);
            if (!used[u]) {
                dfs(u);
            }
        }
        if (array[v].isEmpty()) {
            g[v] = 0;
        } else {
            int ans = 0;
            for (int u : array[v]) {
                ans = ans ^ (g[u] + 1);
            }
            g[v] = ans;

        }
    }

    private static void new_dfs(int v, int k) {
        used[v] = true;
        int cur;
        for (int u : array[v]) {
            cur = g[v] ^ (g[u] + 1) ^ k;
            if (cur == 0) {
                for (int i = 0; i < n - 1; i++) {
                    Pair pair = map.get(i);
                    if ((pair.first == u && pair.second == v) || (pair.first == v && pair.second == u)) {
                        System.out.println(i + 1);
                        System.exit(0);
                    }
                }
            } else {
                if (!used[u]) {
                    new_dfs(u, cur - 1);
                }
            }
        }
    }
}