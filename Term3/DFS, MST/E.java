import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class E {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static ArrayList[] array;
    private static int[] tin;
    private static int[] tout;
    private static int[] up;
    private static int[] used;
    private static int time = -1;
    private static TreeSet<Integer> list = new TreeSet<>();
    private static HashMap<String, Integer> colors = new HashMap<>();
    private static HashMap<Integer, String> numbers = new HashMap<>();

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
            if (child > parent) {
                colors.put(parent + " " + child, 0);
                numbers.put(i, parent + " " + child);
            } else {
                colors.put(child + " " + parent, 0);
                numbers.put(i, child + " " + parent);
            }
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
        used = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                paint(i, -1, mColor);
            }
        }
        System.out.println(mColor);
        for (int i = 0; i < m; i++) {
            System.out.print(colors.get(numbers.get(i)) + " ");
        }
    }

    private static void dfs(int v, int prev) {
        used[v] = 1;
        time++;
        tin[v] = time;
        up[v] = tin[v];
        int count = 0;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (u == prev) {
                continue;
            } else {
                if (used[u] == 0) {
                    dfs(u, v);
                    count++;
                    up[v] = Math.min(up[v], up[u]);
                    if (prev != -1 && up[u] >= tin[v]) {
                        list.add(v);
                    }
                } else {
                    up[v] = Math.min(up[v], tin[u]);
                }
            }
        }
        if (prev == -1 && count >= 2) {
            list.add(v);
        }
    }

    private static void paint(int v, int prev, int color) {
        used[v] = 1;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (u == prev) {
                continue;
            }
            if (used[u] == 0) {
                if (up[u] >= tin[v]) {
                    int clr = ++mColor;
                    if (u > v) {
                        colors.put(v + " " + u, clr);
                    } else {
                        colors.put(u + " " + v, clr);
                    }
                    paint(u, v, clr);
                } else {
                    if (u > v) {
                        colors.put(v + " " + u, color);
                    } else {
                        colors.put(u + " " + v, color);
                    }
                    paint(u, v, color);
                }
            } else {
                if (tin[u] < tin[v]) {
                    if (u > v) {
                        colors.put(v + " " + u, color);
                    } else {
                        colors.put(u + " " + v, color);
                    }
                }
            }
        }
    }
}