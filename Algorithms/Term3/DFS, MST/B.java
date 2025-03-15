import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class B {
    private static int n = 0;
    private static int m = 0;
    private static ArrayList[] array;
    private static int[] tin;
    private static int[] tout;
    private static int[] up;
    private static int[] used;
    private static int time = -1;
    private static HashMap<String, Integer> map;
    private static TreeSet<Integer> list = new TreeSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            array[i] = new ArrayList();
        }
        map = new HashMap<>();
        for (int i = 0; i < m; i++) {
            String str = in.readLine();
            split = str.split("[\\s]");
            int parent, child;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            map.put(str, i);
            array[parent].add(child);
            array[child].add(parent);

        }
        tin = new int[n];
        tout = new int[n];
        used = new int[n];
        up = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs(i, -1);
            }
        }
        System.out.println(list.size());
        for (Integer integer : list) {
            System.out.print(integer + " ");
        }
    }

    private static void dfs(int v, int prev) {
        used[v] = 1;
        time++;
        tin[v] = time;
        up[v] = tin[v];
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (u == prev) {
                continue;
            } else {
                if (used[u] == 0) {
                    dfs(u, v);
                    up[v] = Math.min(up[v], up[u]);
                } else {
                    up[v] = Math.min(up[v], tin[u]);
                }
            }
        }
        if (prev != -1 && up[v] > tin[prev]) {
            if (map.containsKey((v + 1) + " " + (prev + 1))) {
                list.add((map.get((v + 1) + " " + (prev + 1)) + 1));
            } else {
                list.add((map.get((prev + 1) + " " + (v + 1)) + 1));
            }
        }
    }
}