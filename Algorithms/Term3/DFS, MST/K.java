import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class K {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static ArrayList[] array;
    private static ArrayList[] nearray;
    private static int[] used;
    private static int[] colors;
    private static HashSet<Integer> peeks = new HashSet<>();
    private static HashSet<Edge> edges = new HashSet<>();
    private static Stack<Integer> stack = new Stack<>();

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

    private static long findMST(int n, int root) {
        long res = 0;
        int[] minEdge = new int[n];
        for (int i = 0; i < n; i++) {
            minEdge[i] = Integer.MAX_VALUE;
        }
        for (Edge edge : edges) {
            minEdge[edge.to] = Math.min(edge.weight, minEdge[edge.to]);
        }
        for (int i : peeks) {
            if (i == root) {
                continue;
            }
            res += minEdge[i];
        }
        HashSet<Edge> zeroEdges = new HashSet<>();
        array = new ArrayList[n];
        nearray = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            nearray[i] = new ArrayList();
            array[i] = new ArrayList();
        }
        used = new int[n];
        for (Edge edge : edges) {
            if (edge.weight == minEdge[edge.to]) {
                zeroEdges.add(new Edge(edge.from, edge.to, 0));
            }
        }
        for (Edge edge : zeroEdges) {
            array[edge.from].add(edge.to);
            nearray[edge.to].add(edge.from);
        }
        dfs(root);
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return res;
        }
        mColor = 0;
        used = new int[n];
        colors = new int[n];
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                dfs1(i);
            }
        }
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (colors[u] == 0) {
                mColor++;
                dfs2(u);
            }
        }
        peeks.clear();
        for (int i = 0; i < n; i++) {
            colors[i]--;
            peeks.add(colors[i]);
        }
        HashSet<Edge> newEdges = new HashSet<>();
        for (Edge edge : edges) {
            if (colors[edge.to] != colors[edge.from]) {
                newEdges.add(new Edge(colors[edge.from], colors[edge.to], edge.weight - minEdge[edge.to]));
            }
        }
        edges.clear();
        edges.addAll(newEdges);
        res += findMST(mColor, colors[root]);
        return res;
    }

    private static void dfs(int v) {
        used[v] = 1;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (used[u] == 0) {
                dfs(u);
            }
        }
    }

    private static void dfs1(int v) {
        used[v] = 1;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (used[u] == 0) {
                dfs1(u);
            }
        }
        stack.push(v);
    }

    private static void dfs2(int v) {
        colors[v] = mColor;
        for (int i = 0; i < nearray[v].size(); i++) {
            int u = (Integer) nearray[v].get(i);
            if (colors[u] == 0) {
                dfs2(u);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new ArrayList[n];
        nearray = new ArrayList[n];
        peeks = new HashSet<>();
        for (int i = 0; i < n; i++) {
            nearray[i] = new ArrayList();
            array[i] = new ArrayList();
            peeks.add(i);
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child, weight;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            weight = Integer.parseInt(split[2]);
            array[parent].add(child);
            nearray[child].add(parent);
            edges.add(new Edge(parent, child, weight));
        }
        used = new int[n];
        dfs(0);
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            if (used[i] == 0) {
                flag = false;
                break;
            }
        }
        if (!flag) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
            System.out.println(findMST(n, 0));
        }
    }
}