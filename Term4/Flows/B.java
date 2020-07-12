import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class B {

    private static int[][] weights;
    private static HashSet<Integer>[] graph;
    private static HashSet<Integer>[] graph_first;
    private static HashSet<Integer>[] network;
    private static int n;
    private static int[][] edges;
    private static boolean[] used;
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
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        weights = new int[n][n];
        graph = new HashSet[n];
        graph_first = new HashSet[n];
        edges = new int[n][n];
        LinkedList<Edge> list = new LinkedList<>();
        ans = 0;
        for (int i = 0; i < n; i++) {
            graph[i] = new HashSet<>();
            graph_first[i] = new HashSet<>();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int weight = Integer.parseInt(split[2]);
            list.add(new Edge(from, to, weight));
            graph[to].add(from);
            graph_first[to].add(from);
            graph[from].add(to);
            graph_first[from].add(to);
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
        used = new boolean[n];
        dfs1(0);
        HashSet<Integer> set = new HashSet<>();
        for (int j = 0; j < n; j++) {
            if (used[j]) {
                set.add(j);
            }
        }
        for (int j : set) {
            for (int y : graph_first[j]) {
                if (!set.contains(y)) {
                    edges[j][y] = edges[y][j] = 1;
                }
            }
        }
        int i = 0;
        int count = 0;
        LinkedList<Integer> ans_list = new LinkedList<>();
        for (Edge e : list) {
            i++;
            if (edges[e.to][e.from] == 1 || edges[e.from][e.to] == 1) {
                ans_list.add(i);
                count++;
            }
        }
        System.out.println(count + " " + ans);
        for (int e : ans_list) {
            System.out.print(e + " ");
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

    private static void dfs1(int v) {
        used[v] = true;
        for (int u : graph[v]) {
            if (!used[u]) {
                dfs1(u);
            }
        }
    }
}