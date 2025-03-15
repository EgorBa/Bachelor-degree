import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class A {

    private static int[][] weights;
    private static HashSet<Integer>[] graph;
    private static HashSet<Integer>[] network;
    private static int n;
    private static int ans;

    private static class Edge {
        int from;
        int to;
        int weight;

        Edge(int a, int b, int c) {
            from = a;
            to = b;
            weight = c;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(in.readLine());
        int m = Integer.parseInt(in.readLine());
        weights = new int[n][n];
        graph = new HashSet[n];
        LinkedList<Edge> list = new LinkedList<>();
        ans = 0;
        for (int i = 0; i < n; i++) {
            graph[i] = new HashSet<>();
        }
        for (int i = 0; i < m; i++) {
            String[] split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int weight = Integer.parseInt(split[2]);
            list.add(new Edge(from, to, weight));
            graph[to].add(from);
            graph[from].add(to);
            weights[from][to] += weight;
            weights[to][from] += weight;
        }
        while (bfs()) {
            while (true) {
                LinkedList<Integer> list1 = new LinkedList<>();
                list1.add(0);
                boolean flag = dfs(0, list1);
                if (!flag) {
                    break;
                }
            }
        }
        System.out.println(ans);
        for (Edge e : list) {
            int f = (weights[e.to][e.from] - weights[e.from][e.to]) / 2;
            if (f >= 0) {
                f = Math.min(f, e.weight);
            } else {
                f = -Math.min(-f, e.weight);
            }
            System.out.println(f);
            weights[e.to][e.from] -= f;
            weights[e.from][e.to] += f;
        }
    }

    private static boolean bfs() {
        LinkedList<Integer> list = new LinkedList<>();
        int[] d = new int[n];
        Arrays.fill(d, Integer.MAX_VALUE);
        list.add(0);
        d[0] = 0;
        while (!list.isEmpty()) {
            int v = list.getFirst();
            list.removeFirst();
            for (int u : graph[v]) {
                if (d[u] == Integer.MAX_VALUE) {
                    list.addLast(u);
                    d[u] = d[v] + 1;
                }
            }
        }
        if (d[n - 1] == Integer.MAX_VALUE) {
            return false;
        }
        network = new HashSet[n];
        for (int i = 0; i < n; i++) {
            network[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            for (int u : graph[i]) {
                if (d[u] == d[i] + 1) {
                    network[i].add(u);
                }
            }
        }
        return true;
    }

    private static boolean dfs(int v, LinkedList<Integer> path) {
        if (v == n - 1) {
            changeGraph(path);
            return true;
        }
        if (network[v].isEmpty()) {
            path.removeLast();
            if (path.isEmpty()) {
                return false;
            }
            network[path.getLast()].remove(v);
            return dfs(path.getLast(), path);
        } else {
            int u = network[v].iterator().next();
            path.addLast(u);
            return dfs(u, path);
        }
    }

    private static void changeGraph(LinkedList<Integer> path) {
        Iterator<Integer> iter = path.iterator();
        iter.next();
        int min = Integer.MAX_VALUE;
        int from = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int to = iter.next();
            min = Math.min(min, weights[from][to]);
            from = to;
        }
        ans += min;
        iter = path.iterator();
        iter.next();
        from = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int to = iter.next();
            weights[from][to] -= min;
            if (weights[from][to] == 0) {
                graph[from].remove(to);
                network[from].remove(to);
            }
            if (weights[to][from] == 0) {
                graph[to].add(from);
            }
            weights[to][from] += min;
            from = to;
        }
    }
}