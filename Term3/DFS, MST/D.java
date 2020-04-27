import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class D {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static ArrayList[] array;
    private static int[] tin;
    private static int[] tout;
    private static int[] up;
    private static int[] used;
    private static int[] colors;
    private static int time = -1;
    private static HashSet<String> set = new HashSet<>();
    private static HashSet<String> check = new HashSet<>();

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
            if ((set.contains(parent + " " + child) || set.contains(child + " " + parent)) && (!check.contains(parent + " " + child) && !check.contains(child + " " + parent))) {
                check.add(parent + " " + child);
                check.add(child + " " + parent);
            } else {
                set.add(parent + " " + child);
                set.add(child + " " + parent);
                array[parent].add(child);
                array[child].add(parent);
            }
        }
        tin = new int[n];
        tout = new int[n];
        used = new int[n];
        up = new int[n];
        colors = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs(i, -1);
            }
        }
        for (int i = 0; i < n; i++) {
            if (colors[i] == 0) {
                mColor++;
                paint(i, mColor);
            }
        }
        System.out.println(mColor);
        for (int i = 0; i < n; i++) {
            System.out.print(colors[i] + " ");
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
    }

    private static void paint(int v, int color) {
        colors[v] = color;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (colors[u] == 0) {
                if (up[u] > tin[v] && !check.contains(u + " " + v)) {
                    mColor++;
                    paint(u, mColor);
                } else {
                    paint(u, color);
                }
            }
        }
    }
}