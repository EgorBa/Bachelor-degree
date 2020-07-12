import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class C {

    private static int m;
    private static int n;
    private static int e;
    private static int[] px;
    private static int[] py;
    private static int[] ans;
    private static int[] ans1;
    private static boolean[] vis;
    private static boolean[] used;
    private static ArrayList<Integer>[] graph;
    private static ArrayList<Integer>[] graph1;
    private static HashSet<Integer>[] graph3;
    private static int count;
    private static int c;
    private static int[][] weights;
    private static int[][] number;
    private static StringBuilder str = new StringBuilder();

    public static class Int {
        int number;
        int weight;

        Int(int a, int b) {
            this.number = a;
            this.weight = b;
        }

        static Comparator<Int> SalaryComparator = new Comparator<>() {

            @Override
            public int compare(Int e1, Int e2) {
                return -Integer.compare(e1.weight, e2.weight);
            }
        };
    }

    public static void main(String args[]) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        e = Integer.parseInt(split[2]);
        ArrayList<Int> right = new ArrayList<>();
        ArrayList<Int> left = new ArrayList<>();
        ArrayList<Int> all = new ArrayList<>();
        split = in.readLine().split("[\\s]");
        weights = new int[n][m];
        number = new int[n][m];
        for (int i = 0; i < split.length; i++) {
            right.add(new Int(i, Integer.parseInt(split[i])));
            all.add(new Int(i, Integer.parseInt(split[i])));
            Arrays.fill(weights[i], Integer.parseInt(split[i]));
        }
        split = in.readLine().split("[\\s]");
        for (int i = 0; i < split.length; i++) {
            left.add(new Int(i, Integer.parseInt(split[i])));
            all.add(new Int(i + n, Integer.parseInt(split[i])));
            for (int j = 0; j < n; j++) {
                weights[j][i] += Integer.parseInt(split[i]);
            }
        }
        right.sort(Int.SalaryComparator);
        left.sort(Int.SalaryComparator);
        all.sort(Int.SalaryComparator);
        graph = new ArrayList[n];
        graph1 = new ArrayList[m];
        graph3 = new HashSet[n + m];
        for (int i = 0; i < n + m; i++) {
            graph3[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            graph1[i] = new ArrayList<>();
        }
        for (int i = 0; i < e; i++) {
            split = in.readLine().split("[\\s]");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            number[x - 1][y - 1] = i + 1;
            graph[x - 1].add(y - 1);
            graph1[y - 1].add(x - 1);
        }
        ans = new int[m];
        used = new boolean[n];
        Arrays.fill(ans, -1);
        for (int i = 0; i < n; i++) {
            Arrays.fill(used, false);
            dfs3(right.get(i).number);
        }
        for (int i = 0; i < m; i++) {
            if (ans[i] != -1) {
                graph3[ans[i]].add(i + n);
                graph3[i + n].add(ans[i]);
            }
        }
        ans = new int[n];
        used = new boolean[m];
        Arrays.fill(ans, -1);
        for (int i = 0; i < m; i++) {
            Arrays.fill(used, false);
            dfs4(left.get(i).number);
        }
        for (int i = 0; i < n; i++) {
            if (ans[i] != -1) {
                graph3[i].add(ans[i] + n);
                graph3[ans[i] + n].add(i);
            }
        }
        vis = new boolean[n + m];
        count = 0;
        c = 0;
        for (int i = 0; i < n + m; i++) {
            int v = all.get(i).number;
            if (graph3[v].size() == 1 && !vis[v]) {
                dfs2(v, -1);
            }
        }
        for (int i = 0; i < n + m; i++) {
            if (!vis[i]) {
                dfs2(i, -1);
            }
        }
        System.out.println(count);
        System.out.println(c);
        System.out.println(str);
        StringBuilder str1 = new StringBuilder();
        str1.append(-c).append('\n').append(count).append('\n').append(str.toString());
    }

    private static boolean dfs3(int v) {
        if (used[v]) {
            return false;
        }
        used[v] = true;
        for (int to : graph[v]) {
            if (ans[to] == -1 || dfs3(ans[to])) {
                ans[to] = v;
                return true;
            }
        }
        return false;
    }

    private static boolean dfs4(int v) {
        if (used[v]) {
            return false;
        }
        used[v] = true;
        for (int to : graph1[v]) {
            if (ans[to] == -1 || dfs4(ans[to])) {
                ans[to] = v;
                return true;
            }
        }
        return false;
    }

    private static void dfs2(int i, int chet) {
        if (vis[i]) {
            return;
        }
        vis[i] = true;
        chet++;
        for (int j : graph3[i]) {
            if (vis[j]) {
                continue;
            }
            if (chet % 2 == 0) {
                if (i >= n) {
                    count += weights[j][i - n];
                    c++;
                    str.append(String.valueOf(number[j][i - n])).append(" ");
                } else {
                    count += weights[i][j - n];
                    c++;
                    str.append(String.valueOf(number[i][j - n])).append(" ");
                }
            }
            dfs2(j, chet);
        }
    }
}
