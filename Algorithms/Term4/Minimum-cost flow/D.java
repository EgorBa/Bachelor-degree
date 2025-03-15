import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class D {

    private static ArrayList<Edge>[] graph;
    private static ArrayList<Edge>[] network;
    private static int n;
    private static int s;
    private static int t;
    private static double ans;
    private static boolean[] used;

    private static class Edge {
        int from;
        int to;
        long weight;
        long cost;
        boolean del;
        Edge back;

        Edge(int a, int b, long c, long d) {
            from = a;
            to = b;
            weight = c;
            cost = d;
            del = weight == 0;
        }

        public String toString() {
            return "Edge: " + from + " " + to + " " + weight + " " + cost + " " + del;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        s = 2 * n;
        t = s + 1;
        int m = Integer.parseInt(split[1]);
        graph = new ArrayList[2 * n + 2];
        ans = 0;
        for (int i = 0; i < 2 * n + 2; i++) {
            graph[i] = new ArrayList<>();
        }
        split = in.readLine().split("[\\s]");
        for (int i = 0; i < n; i++) {
            int c = Integer.parseInt(split[i]);
            addEdge(i, i + n, Integer.MAX_VALUE, c);
            addEdge(i + n, i, Integer.MAX_VALUE, 0);
            addEdge(s, i, 1, 0);
            addEdge(i + n, t, 1, 0);
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int c = Integer.parseInt(split[2]);
            addEdge(from, n + to, Integer.MAX_VALUE, c);
        }
        n = 2 * n + 2;
        while (bfs()) {
            used = new boolean[n];
            LinkedList<Edge> list1 = new LinkedList<>();
            dfs(s, list1);
        }
        System.out.println((long) ans);
    }

    private static void addEdge(int from, int to, int weight, int c) {
        Edge e = new Edge(from, to, weight, c);
        Edge e1 = new Edge(to, from, 0, -c);
        e.back = e1;
        e1.back = e;
        graph[e.from].add(e);
        graph[e1.from].add(e1);
    }

    private static void printGraph(ArrayList<Edge>[] network) {
        for (int i = 0; i < n; i++) {
            for (Edge e : network[i]) {
                System.out.print(e.toString() + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    private static boolean bfs() {
        double[] d = new double[n];
        int[] visited = new int[n];
        Edge[] p = new Edge[n];
        Arrays.fill(d, Double.POSITIVE_INFINITY);
        d[s] = 0;
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        while (!queue.isEmpty()) {
            int v = queue.getFirst();
            queue.removeFirst();
            visited[v] = 2;
            for (Edge e : graph[v]) {
                if (!e.del && d[e.to] > d[e.from] + e.cost) {
                    d[e.to] = d[e.from] + e.cost;
                    if (visited[e.to] == 0) {
                        queue.addLast(e.to);
                    } else {
                        if (visited[e.to] == 2) {
                            queue.addFirst(e.to);
                        }
                    }
                    p[e.to] = e;
                    visited[e.to] = 1;
                }
            }
        }
        if (d[t] == Double.POSITIVE_INFINITY) {
            return false;
        }
        network = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            network[i] = new ArrayList<>();
        }
        for (Edge edge : p) {
            if (edge != null) {
                network[edge.from].add(edge);
            }
        }
        return true;
    }

    private static void dfs(int v, LinkedList<Edge> path) {
        if (v == t) {
            changeGraph(path);
        }
        used[v] = true;
        for (Edge e : network[v]) {
            if (!used[e.to]) {
                path.add(e);
                dfs(e.to, path);
                path.removeLast();
            }
        }
    }

    private static void changeGraph(LinkedList<Edge> path) {
        Iterator<Edge> iter = path.iterator();
        long min = Long.MAX_VALUE;
        for (int i = 0; i < path.size(); i++) {
            Edge e = iter.next();
            min = Math.min(min, e.weight);
        }
        iter = path.iterator();
        for (int i = 0; i < path.size(); i++) {
            Edge e = iter.next();
            ans += min * e.cost;
        }
        iter = path.iterator();
        for (int i = 0; i < path.size(); i++) {
            Edge e = iter.next();
            e.weight -= min;
            e.del = e.weight == 0;
            e.back.weight += min;
            e.back.del = e.back.weight == 0;
        }
    }
}