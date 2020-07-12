import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class F {

    private static int[][] weights;
    private static int[] weight;
    private static HashSet<Integer>[] graph;
    private static HashSet<Integer>[] network;
    private static Character[][] matrix;
    private static int n;
    private static int N;
    private static int ans;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(in.readLine());
        matrix = new Character[n][n];
        N = n + 2 + (n * n - n) / 2;
        weights = new int[N][N];
        weight = new int[n];
        graph = new HashSet[N];
        ans = 0;
        for (int i = 0; i < N; i++) {
            graph[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            String str = in.readLine();
            for (int j = 0; j < n; j++) {
                matrix[i][j] = str.charAt(j);
            }
        }
        String[] split = in.readLine().split("[\\s]");
        for (int i = 0; i < n; i++) {
            weight[i] = Integer.parseInt(split[i]);
        }
        int count = n + 2;
        for (int from = 0; from < n + 2; from++) {
            if (from == 0) {
                for (int to = n + 2; to < n + 2 + (n * n - n) / 2; to++) {
                    graph[from].add(to);
                    weights[from][to] += 3;
                }
                continue;
            }
            if (from == n + 1) {
                for (int to = 1; to <= n; to++) {
                    graph[to].add(from);
                    weights[to][from] += weight[to - 1];
                }
                continue;
            }
            for (int j = from + 1; j <= n; j++) {
                if (matrix[from - 1][j - 1] == 'W') {
                    graph[count].add(j);
                    graph[count].add(from);
                    weights[count][from] += 3;
                    weights[count][j] += 0;
                } else {
                    if (matrix[from - 1][j - 1] == 'w') {
                        graph[count].add(j);
                        graph[count].add(from);
                        weights[count][from] += 2;
                        weights[count][j] += 1;
                    } else {
                        if (matrix[from - 1][j - 1] == 'L') {
                            graph[count].add(j);
                            graph[count].add(from);
                            weights[count][from] += 0;
                            weights[count][j] += 3;
                        } else {
                            if (matrix[from - 1][j - 1] == 'l') {
                                graph[count].add(j);
                                graph[count].add(from);
                                weights[count][from] += 1;
                                weights[count][j] += 2;
                            } else {
                                graph[count].add(j);
                                graph[count].add(from);
                                weights[count][from] += 3;
                                weights[count][j] += 3;
                            }
                        }
                    }
                }
                count++;
            }
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
        count = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j > i) {
                    count++;
                }
                if (matrix[i][j] == '.') {
                    matrix[i][j] = findWeight(count, i + 1);
                    matrix[j][i] = findWeight(count, j + 1);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static Character findWeight(int from, int to) {
        if (weights[from][to] == 1) {
            return 'w';
        }
        if (weights[from][to] == 0) {
            return 'W';
        }
        if (weights[from][to] == 2) {
            return 'l';
        }
        if (weights[from][to] == 3) {
            return 'L';
        }
        return '#';
    }

    private static boolean bfs() {
        LinkedList<Integer> list = new LinkedList<>();
        int[] d = new int[N];
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
        if (d[n + 1] == Integer.MAX_VALUE) {
            return false;
        }
        network = new HashSet[N];
        for (int i = 0; i < N; i++) {
            network[i] = new HashSet<>();
        }
        for (int i = 0; i < N; i++) {
            for (int u : graph[i]) {
                if (d[u] == d[i] + 1) {
                    network[i].add(u);
                }
            }
        }
        return true;
    }

    private static boolean dfs(int v, LinkedList<Integer> path) {
        if (v == n + 1) {
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