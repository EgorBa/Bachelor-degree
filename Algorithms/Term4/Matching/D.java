import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public class D {

    private static HashSet<Integer>[] graph;
    private static HashSet<Integer>[] graph1;
    private static int[] px;
    private static int[] py;
    private static boolean[] vis;
    private static boolean[] used;
    private static int n;
    private static int m;
    private static int l;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("birthday.in"));
        PrintWriter out = new PrintWriter("birthday.out");
        String[] split = in.readLine().split("[\\s]");
        l = Integer.parseInt(split[0]);
        for (int k = 0; k < l; k++) {
            split = in.readLine().split("[\\s]");
            n = Integer.parseInt(split[0]);
            m = Integer.parseInt(split[1]);
            graph = new HashSet[n + m];
            graph1 = new HashSet[n + m];
            for (int j = 0; j < n + m; j++) {
                graph[j] = new HashSet<>();
                graph1[j] = new HashSet<>();
            }
            for (int i = 0; i < n; i++) {
                split = in.readLine().split("[\\s]");
                for (int j = 0; j < split.length - 1; j++) {
                    graph[i].add(Integer.parseInt(split[j]) + n - 1);
                    graph[Integer.parseInt(split[j]) + n - 1].add(i);
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = n; j < n + m; j++) {
                    if (graph[i].contains(j)) {
                        graph[i].remove(j);
                    } else {
                        graph[i].add(j);
                    }
                }
            }
            for (int i = n; i < n + m; i++) {
                for (int j = 0; j < n; j++) {
                    if (graph[i].contains(j)) {
                        graph[i].remove(j);
                    } else {
                        graph[i].add(j);
                    }
                }
            }
            px = new int[n];
            py = new int[m];
            vis = new boolean[m + n];
            used = new boolean[m + n];
            Arrays.fill(vis, false);
            Arrays.fill(used, false);
            Arrays.fill(px, -1);
            Arrays.fill(py, -1);
            boolean isPath = true;
            while (isPath) {
                isPath = false;
                Arrays.fill(vis, false);
                for (int i = 0; i < n; i++) {
                    if (px[i] == -1) {
                        if (dfs(i)) {
                            isPath = true;
                        }
                    }
                }
            }
            HashSet<Integer> max = new HashSet<>();
            for (int i = 0; i < n + m; i++) {
                max.add(i);
            }
            HashSet<Integer> set = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if (px[i] != -1) {
                    graph1[px[i]].add(i);
                    set.add(i);
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j : graph[i]) {
                    if (!graph1[j].contains(i)) {
                        graph1[i].add(j);
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                if (!set.contains(j)) {
                    dfs1(j);
                }
            }
            set = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if (!used[i]) {
                    set.add(i);
                }
            }
            for (int i = n; i < n + m; i++) {
                if (used[i]) {
                    set.add(i);
                }
            }
            max.removeAll(set);
            int boys_count = 0;
            int girls_count = 0;
            TreeSet<Integer> boys = new TreeSet<>();
            TreeSet<Integer> girls = new TreeSet<>();
            for (int i : max) {
                if (i >= n) {
                    girls_count++;
                    girls.add(i - n + 1);
                } else {
                    boys.add(i + 1);
                    boys_count++;
                }
            }
            System.out.println(girls_count + boys_count);
            out.println(girls_count + boys_count);
            System.out.println(boys_count + " " + girls_count);
            out.println(boys_count + " " + girls_count);
            for (int i : boys) {
                System.out.print(i + " ");
                out.print(i + " ");
            }
            System.out.println();
            out.println();
            for (int i : girls) {
                System.out.print(i + " ");
                out.print(i + " ");
            }
            System.out.println();
            out.println();
        }
        in.close();
        out.close();
    }

    private static void dfs1(int i) {
        if (used[i]) {
            return;
        }
        used[i] = true;
        for (int j : graph1[i]) {
            dfs1(j);
        }
    }

    private static boolean dfs(int i) {
        if (vis[i]) {
            return false;
        }
        vis[i] = true;
        for (int j : graph[i]) {
            if (py[j - n] == -1) {
                py[j - n] = i;
                px[i] = j;
                return true;
            } else {
                if (dfs(py[j - n])) {
                    py[j - n] = i;
                    px[i] = j;
                    return true;
                }
            }
        }
        return false;
    }
}