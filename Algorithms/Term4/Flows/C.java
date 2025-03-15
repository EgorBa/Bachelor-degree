import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class C {

    private static ArrayList<Edge>[] graph;
    private static int n;
    private static boolean[] visited;
    private static int ans;
    private static int s;
    private static int t;

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
        s = Integer.parseInt(split[2]) - 1;
        t = Integer.parseInt(split[3]) - 1;
        int m = Integer.parseInt(split[1]);
        graph = new ArrayList[n];
        ans = 0;
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            Edge e1 = new Edge(from, to, 1, 0);
            Edge e2 = new Edge(to, from, 0, 0);
            e2.back = e1;
            e1.back = e2;
            graph[from].add(e1);
            graph[to].add(e2);
        }
        while (true) {
            visited = new boolean[n];
            int delta = dfs(s, Integer.MAX_VALUE);
            if (delta > 0) {
                ans += delta;
            } else {
                break;
            }
            if (ans >= 2) {
                break;
            }
        }
        if (ans < 2) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
            int to = s;
            while (to != t) {
                System.out.print((to + 1) + " ");
                for (Edge tube : graph[to]) {
                    if (!tube.del && tube.f == 1) {
                        tube.del = true;
                        to = tube.to;
                        break;
                    }
                }
            }
            to = s;
            System.out.println(t + 1);
            while (to != t) {
                System.out.print((to + 1) + " ");
                for (Edge tube : graph[to]) {
                    if (!tube.del && tube.f == 1) {
                        tube.del = true;
                        to = tube.to;
                        break;
                    }
                }
            }
            System.out.println(t + 1);
        }
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

    private static LinkedList<Integer> dfs1(int v, LinkedList<Integer> path) {
        visited[v] = true;
        if (v == t) {
            return path;
        }
        for (Edge e : graph[v]) {
            if (!visited[e.to] && e.f != 0 && !e.del) {
                e.del = true;
                path.add(e.to);
                return dfs1(e.to, path);
            }
        }
        return new LinkedList<>();
    }

}