import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class J {
    private static int n = 0;
    private static int m = 0;
    private static HashSet<Integer>[] array;
    private static int[] tin;
    private static int[] g;
    private static int[] tout;
    private static int[] used;
    private static int time = -1;
    private static boolean flag = false;
    private static int[] sort;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new HashSet[n];
        for (int i = 0; i < n; i++) {
            array[i] = new HashSet();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            array[parent].add(child);
        }
        tin = new int[n];
        g = new int[n];
        tout = new int[n];
        used = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs(i);
            }
        }
        sort = new int[2 * n];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            sort[tout[i]]++;
            map.put(tout[i], i);
        }
        for (int i = 0; i <= 2 * n - 1; i++) {
            if (sort[i] == 1) {
                int v = map.get(i);
                if (array[v].isEmpty()) {
                    g[v] = 0;
                } else {
                    TreeSet<Integer> set = new TreeSet<>();
                    for (int u : array[v]) {
                        set.add(g[u]);
                    }
                    int min = 0;
                    for (int u : set) {
                        if (u == min) {
                            min++;
                        } else {
                            break;
                        }
                    }
                    g[v] = min;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.println(g[i]);
        }

    }

    private static void dfs(int v) {
        used[v] = 1;
        time++;
        tin[v] = time;
        for (int u : array[v]) {
            if (used[u] == 1) {
                flag = true;
            } else {
                if (used[u] == 0) {
                    dfs(u);
                }
            }
        }
        used[v] = 2;
        time++;
        tout[v] = time;
    }
}