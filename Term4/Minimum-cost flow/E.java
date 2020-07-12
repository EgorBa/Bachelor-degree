import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class E {

    private static ArrayList<Edge>[] graph;
    private static ArrayList<Edge>[] network;
    private static int n;
    private static double cost;
    private static double flow;
    private static boolean[] used;

    private static class Edge {
        int flow;
        int number;
        int from;
        int to;
        long weight;
        long cost;
        boolean del;
        Edge back;

        Edge(int a, int b, long c, long d, int n) {
            from = a;
            to = b;
            weight = c;
            cost = d;
            del = weight == 0;
            number = n;
            flow = 0;
        }

        public String toString() {
            return "Edge: " + from + " " + to + " " + weight + " " + cost + " " + del;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int k = Integer.parseInt(split[2]);
        graph = new ArrayList[n];
        cost = 0;
        flow = 0;
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            int c = Integer.parseInt(split[2]);
            addEdge(from, to, c, i + 1);
            addEdge(to, from, c, i + 1);
        }
        while (bfs()) {
            used = new boolean[n];
            LinkedList<Edge> list1 = new LinkedList<>();
            dfs(0, list1);
            if (flow >= k) {
                break;
            }
        }
        if (flow < k) {
            System.out.println(-1);
            System.exit(0);
        }
        System.out.println(cost / k);
        HashSet<ArrayList<Integer>> set = new HashSet<>();
        for (int i = 0; i < k; i++) {
            used = new boolean[n];
            ArrayList<Integer> list = findPath(0, new ArrayList<>());
            set.add(list);
        }
        for (ArrayList<Integer> list : set) {
            System.out.print(list.size() + " ");
            for (int i : list) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    private static ArrayList<Integer> findPath(int v, ArrayList<Integer> path) {
        used[v] = true;
        for (Edge e : graph[v]) {
            if (!used[e.to] && e.flow > 0) {
                e.flow = 0;
                path.add(e.number);
                e.del = true;
                return findPath(e.to, path);
            }
        }
        return path;
    }

    private static void addEdge(int from, int to, int c, int number) {
        Edge e = new Edge(from, to, 1, c, number);
        Edge e1 = new Edge(to, from, 0, -c, number);
        e.back = e1;
        e1.back = e;
        graph[e.from].add(e);
        graph[e1.from].add(e1);
    }

    private static boolean bfs() {
        double[] d = new double[n];
        int[] visited = new int[n];
        Edge[] p = new Edge[n];
        Arrays.fill(d, Double.POSITIVE_INFINITY);
        d[0] = 0;
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(0);
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
        if (d[n - 1] == Double.POSITIVE_INFINITY) {
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
        if (v == n - 1) {
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
        flow += min;
        iter = path.iterator();
        for (int i = 0; i < path.size(); i++) {
            Edge e = iter.next();
            cost += min * e.cost;
        }
        iter = path.iterator();
        for (int i = 0; i < path.size(); i++) {
            Edge e = iter.next();
            e.weight -= min;
            e.flow += min;
            e.back.flow -= min;
            e.del = e.weight == 0;
            e.back.weight += min;
            e.back.del = e.back.weight == 0;
        }
    }
}