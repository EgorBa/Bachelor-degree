import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class E {

    private static ArrayList<Edge>[] graph;
    private static int n;
    private static int N = 15000;
    private static boolean[] visited;
    private static int ans;
    private static int s;
    private static int t;
    private static int k;

    private static class Edge {
        int from;
        int to;
        int c;
        int f;
        Edge back;
        boolean del;

        Edge(int a, int b, int c, int f) {
            from = a;
            to = b;
            this.c = c;
            this.f = f;
            del = false;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        k = Integer.parseInt(split[2]);
        s = Integer.parseInt(split[3]) - 1;
        t = Integer.parseInt(split[4]) - 1;
        graph = new ArrayList[N];
        HashSet<Integer>[] gr = new HashSet[N];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
            gr[i] = new HashSet<>();
        }
        ArrayList<Edge> list = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            list.add(new Edge(from, to, 0, 0));
            list.add(new Edge(to, from, 0, 0));
        }
        int sum = 0;
        int days = 0;
        while (sum < k) {
            days++;
            for (int i = 0; i < n; i++) {
                int from = (days - 1) * n + i;
                int to = days * n + i;
                add(from, to, Integer.MAX_VALUE);
            }
            for (Edge e : list) {
                int from = (days - 1) * n + e.from;
                int to = days * n + e.to;
                add(from, to, 1);
            }
            t = t + n;
            ans = 0;
            while (true) {
                visited = new boolean[N];
                int delta = dfs(s, Integer.MAX_VALUE);
                if (delta > 0) {
                    ans += delta;
                } else {
                    break;
                }
            }
            sum += ans;
        }
        System.out.println(days);
        int d = 0;
        ArrayList<String> strs = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            gr[s].add(i);
        }
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < n; j++) {
                for (Edge e : graph[d * n + j]) {
                    if (e.f > 0) {
                        Iterator<Integer> iter = gr[e.from].iterator();
                        HashSet<Integer> set = new HashSet<>();
                        for (int y = 0; y < Math.min(e.f, gr[e.from].size()); y++) {
                            int u = iter.next();
                            set.add(u);
                            gr[e.to].add(u);
                        }
                        gr[d * n + j].removeAll(set);
                    }
                }
            }
            int cnt = 0;
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j == s) {
                    continue;
                }
                int u = (d + 1) * n + j;
                if (!gr[u].isEmpty()) {
                    for (int v : gr[u]) {
                        str.append(v).append(" ").append(j + 1).append("  ");
                        cnt++;
                    }
                }
            }
            d++;
            strs.add(cnt + "  " + str);
        }
        for (int i = 0; i < days; i++) {
            System.out.println(strs.get(i));
        }
    }

    private static void add(int from, int to, int c) {
        Edge e1 = new Edge(from, to, c, 0);
        Edge e2 = new Edge(to, from, 0, 0);
        e2.back = e1;
        e1.back = e2;
        graph[from].add(e1);
        graph[to].add(e2);
    }

    private static int dfs(int u, int Cmin) {
        if (u == t) {
            return Cmin;
        }
        if (visited[u]) {
            return 0;
        }
        visited[u] = true;
        for (Edge e : graph[u]) {
            if (e.f < e.c) {
                int delta = dfs(e.to, Math.min(e.c - e.f, Cmin));
                if (delta > 0) {
                    e.f += delta;
                    e.back.f -= delta;
                    return delta;
                }
            }
        }
        return 0;
    }

}