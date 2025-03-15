import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class H {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static ArrayList[] array;
    private static ArrayList[] comp;
    private static ArrayList[] nearray;
    private static int[] colors;
    private static int[] used;
    private static int[] sort;
    private static Stack<Integer> stack = new Stack<>();
    private static TreeMap<Integer, ArrayList<Pair>> map = new TreeMap<>();
    private static HashMap<Integer, ArrayList<Pair>> map1 = new HashMap<>();

    private static class Pair {
        int from;
        int to;

        Pair(int i, int j) {
            from = i;
            to = j;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("avia.in"));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        int ans[][] = new int[n + 1][n + 1];
        sort = new int[n * n];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                ans[i][j] = Integer.parseInt(split[j]) + 1;
                sort[i * n + j] = ans[i][j];
                if (map1.containsKey(ans[i][j])) {
                    ArrayList list = map1.get(ans[i][j]);
                    list.add(new Pair(i, j));
                    map1.put(ans[i][j], list);
                } else {
                    ArrayList list = new ArrayList();
                    list.add(new Pair(i, j));
                    map1.put(ans[i][j], list);
                }
            }
        }
        Arrays.sort(sort);
        array = new ArrayList[n];
        nearray = new ArrayList[n];
        colors = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = new ArrayList();
            nearray[i] = new ArrayList();
        }
        mColor = n;
        for (int i = 0; i < n; i++) {
            colors[i] = i + 1;
        }
        int answer = 0;
        int count = 0;
        while (mColor != 1) {
            while (sort[count] <= answer) {
                count++;
            }
            for (Pair pair : map1.get(sort[count])) {
                array[pair.from].add(pair.to);
                nearray[pair.to].add(pair.from);
                answer = sort[count];
            }
            map1.remove(sort[count]);
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
        }
        in.close();
        PrintWriter out = new PrintWriter("avia.out");
        out.println((answer - 1) < 0 ? 0 : answer - 1);
        out.close();
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

}