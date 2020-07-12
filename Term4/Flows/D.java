import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class D {

    private static long[][] weights;
    private static Character[][] matrix;
    private static HashSet<Integer>[] graph;
    private static HashSet<Integer>[] graph_first;
    private static HashSet<Integer>[] network;
    private static int n;
    private static int m;
    private static int s;
    private static int t;
    private static int[][] edges;
    private static boolean[] used;
    private static long ans;
    private static LinkedList<Edge> list;

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
        m = Integer.parseInt(split[1]);
        weights = new long[n * m * 2][n * m * 2];
        graph = new HashSet[n * m * 2];
        matrix = new Character[n][m];
        graph_first = new HashSet[n * m * 2];
        edges = new int[n * m * 2][n * m * 2];
        list = new LinkedList<>();
        ans = 0;
        for (int i = 0; i < n * m * 2; i++) {
            graph[i] = new HashSet<>();
            graph_first[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            String str = in.readLine();
            for (int j = 0; j < m; j++) {
                matrix[i][j] = str.charAt(j);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == 'A') {
                    s = i * m + j;
                }
                if (matrix[i][j] == 'B') {
                    t = i * m + j;
                }
                if (matrix[i][j] == '#') {
                    continue;
                }
                if (matrix[i][j] != '.') {
                    builder(i, j, 1, 0);
                    builder(i, j, -1, 0);
                    builder(i, j, 0, 1);
                    builder(i, j, 0, -1);
                } else {
                    int to = i * m + j;
                    int from = to + n * m;
                    build_double(to, from);
                    list.add(new Edge(to, from, 1));
                    builder_point(i, j, 1, 0, true);
                    builder_point(i, j, -1, 0, true);
                    builder_point(i, j, 0, 1, true);
                    builder_point(i, j, 0, -1, true);
                    builder_point(i, j, 1, 0, false);
                    builder_point(i, j, -1, 0, false);
                    builder_point(i, j, 0, 1, false);
                    builder_point(i, j, 0, -1, false);
                }
            }
        }
        while (bfs()) {
            while (true) {
                LinkedList<Integer> list1 = new LinkedList<>();
                list1.add(s);
                boolean flag = dfs(s, list1);
                if (!flag) {
                    break;
                }
            }
        }
        used = new boolean[2 * n * m];
        if (ans >= Integer.MAX_VALUE) {
            System.out.println(-1);
        } else {
            System.out.println(ans);
            dfs1(s);
            HashSet<Integer> set = new HashSet<>();
            for (int j = 0; j < n * m * 2; j++) {
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
            for (Edge e : list) {
                if (edges[e.to][e.from] == 1 || edges[e.from][e.to] == 1) {
                    int u = Math.min(e.from, e.to);
                    matrix[(u - (u % m)) / m][u % m] = '+';
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(matrix[i][j]);
                }
                System.out.println();
            }
        }
    }

    private static void build(int from, int to) {
        graph[from].add(to);
        graph_first[from].add(to);
        weights[from][to] += Integer.MAX_VALUE;
    }

    private static void builder(int i, int j, int dif_i, int dif_j) {
        int from = i * m + j;
        int to = (i + dif_i) * m + j + dif_j;
        if (j + dif_j < m && j + dif_j >= 0 && i + dif_i < n && i + dif_i >= 0 && matrix[i + dif_i][j + dif_j] != '#' && matrix[i + dif_i][j + dif_j] != '.') {
            build(from, to);
            build(to, from);
        } else {
            if (j + dif_j < m && j + dif_j >= 0 && i + dif_i < n && i + dif_i >= 0 && matrix[i + dif_i][j + dif_j] != '#') {
                build(from, to);
                build(to + (n * m), from);
            }
        }
    }

    private static void builder_point(int i, int j, int dif_i, int dif_j, boolean flag) {
        if (flag) {
            int from = i * m + j;
            int to = (i + dif_i) * m + j + dif_j;
            if (j + dif_j < m && j + dif_j >= 0 && i + dif_i < n && i + dif_i >= 0 && matrix[i + dif_i][j + dif_j] != '#' && matrix[i + dif_i][j + dif_j] != '.') {
                build(to, from);
            } else {
                if (j + dif_j < m && j + dif_j >= 0 && i + dif_i < n && i + dif_i >= 0 && matrix[i + dif_i][j + dif_j] != '#') {
                    build(to + (n * m), from);
                }
            }
        } else {
            int from = i * m + j + n * m;
            int to = (i + dif_i) * m + j + dif_j;
            if (j + dif_j < m && j + dif_j >= 0 && i + dif_i < n && i + dif_i >= 0 && matrix[i + dif_i][j + dif_j] != '#') {
                build(from, to);
            }
        }
    }

    private static void build_double(int from, int to) {
        graph[to].add(from);
        graph_first[to].add(from);
        graph[from].add(to);
        graph_first[from].add(to);
        weights[from][to] += 1;
        weights[to][from] += 1;
    }

    private static boolean bfs() {
        LinkedList<Integer> list = new LinkedList<>();
        int[] d = new int[n * m * 2];
        Arrays.fill(d, Integer.MAX_VALUE);
        list.add(s);
        d[s] = s;
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
        if (d[t] == Integer.MAX_VALUE) {
            return false;
        }
        network = new HashSet[n * m * 2];
        for (int i = 0; i < n * m * 2; i++) {
            network[i] = new HashSet<>();
        }
        for (int i = 0; i < n * m * 2; i++) {
            for (int u : graph[i]) {
                if (d[u] == d[i] + 1) {
                    network[i].add(u);
                }
            }
        }
        return true;
    }

    private static boolean dfs(int v, LinkedList<Integer> path) {
        if (v == t) {
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
        long min = Integer.MAX_VALUE;
        int from = s;
        for (int i = 0; i < path.size() - 1; i++) {
            int to = iter.next();
            min = Math.min(min, weights[from][to]);
            from = to;
        }
        ans += min;
        iter = path.iterator();
        iter.next();
        from = s;
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