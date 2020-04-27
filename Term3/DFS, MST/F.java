import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class F {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static ArrayList[] array;
    private static ArrayList[] nearray;
    private static int[] tin;
    private static int[] tout;
    private static int[] up;
    private static int[] colors;
    private static int[] used;
    private static Stack<Integer> stack = new Stack<>();
    private static HashSet<String> ans = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new ArrayList[n];
        nearray = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            array[i] = new ArrayList();
            nearray[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            array[parent].add(child);
            nearray[child].add(parent);
        }
        tin = new int[n];
        tout = new int[n];
        used = new int[n];
        up = new int[n];
        colors = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs1(i);
            }
        }
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (colors[u] == 0) {
                mColor++;
                dfs2(u);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < array[i].size(); j++) {
                int u = (Integer) array[i].get(j);
                if (colors[i] == colors[u]) {
                    continue;
                }
                if (colors[i] < colors[u]) {
                    ans.add(colors[i] + " " + colors[u]);
                } else {
                    ans.add(colors[u] + " " + colors[i]);
                }
            }
        }
        System.out.println(ans.size());
    }

    private static void dfs1(int v) {
        used[v] = 1;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (used[u] == 0) {
                dfs1(u);
            }
        }
        stack.push(v);
    }

    private static void dfs2(int v) {
        colors[v] = mColor;
        for (int i = 0; i < nearray[v].size(); i++) {
            int u = (Integer) nearray[v].get(i);
            if (colors[u] == 0) {
                dfs2(u);
            }
        }
    }

}