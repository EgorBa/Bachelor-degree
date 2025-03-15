import java.io.*;
import java.util.*;

public class H {
    private static ArrayList<Integer>[] list;
    private static boolean[] used;
    private static int[] d;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("game.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int s = Integer.parseInt(split[2]) - 1;
        list = new ArrayList[n];
        d = new int[n];
        used = new boolean[n];
        for (int i = 0; i < n; i++) {
            d[i] = Integer.MAX_VALUE;
            list[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int from = Integer.parseInt(split[0]) - 1;
            int to = Integer.parseInt(split[1]) - 1;
            list[from].add(to);
        }
        in.close();
        PrintWriter out = new PrintWriter("game.out");
        dfs(s);
        if (d[s] == 0) {
            out.println("Second player wins");
        } else {
            out.println("First player wins");
        }
        out.close();
    }

    private static void dfs(int v) {
        used[v] = true;
        for (int i = 0; i < list[v].size(); i++) {
            int u = list[v].get(i);
            if (!used[u]) {
                dfs(u);
            }
        }
        if (list[v].size() == 0) {
            d[v] = 0;
        } else {
            boolean flag = true;
            for (int i = 0; i < list[v].size(); i++) {
                int u = list[v].get(i);
                if (d[u] == 0) {
                    d[v] = 1;
                    flag = false;
                }
            }
            if (flag) {
                d[v] = 0;
            }
        }
    }
}