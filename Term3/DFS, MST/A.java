import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class A {
    private static int n = 0;
    private static int m = 0;
    private static ArrayList[] array;
    private static int[] tin;
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
        array = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            array[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            array[parent].add(child);
        }
        tin = new int[n];
        tout = new int[n];
        used = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs(i);
            }
        }
        if (flag) {
            System.out.println(-1);
        } else {
            sort = new int[2 * n];
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                sort[tout[i]]++;
                map.put(tout[i], i);
            }
            for (int i = 2 * n - 1; i >= 0; i--) {
                if (sort[i] == 1) {
                    System.out.print((map.get(i) + 1) + " ");
                }
            }
        }
    }

    private static void dfs(int v) {
        used[v] = 1;
        time++;
        tin[v] = time;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
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