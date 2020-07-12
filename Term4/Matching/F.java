import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class F {

    private static ArrayList<Integer>[] graph;
    private static int[] px ;
    private static int[] py ;
    private static boolean[] [] free;
    private static boolean[] vis ;
    private static int m;
    private static int n;
    private static int a;
    private static int b;

    public static void main (String[]args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("dominoes.in"));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        a = Integer.parseInt(split[2]);
        b = Integer.parseInt(split[3]);
        free = new boolean[n][m];
        graph = new ArrayList[n * m];
        for (int i = 0; i < n * m; i++) {
            graph[i] = new ArrayList<>();
        }
        int count_free = 0;
        for (int i = 0; i < n; i++) {
            String str = in.readLine();
            for (int j = 0; j < m; j++) {
                free[i][j] = str.charAt(j) != '.';
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (free[i][j]) {
                    count_free++;
                    if (j + 1 < m && free[i][j + 1]) {
                        graph[i * m + j].add(i * m + j + 1);
                    }
                    if (j - 1 > -1 && free[i][j - 1]) {
                        graph[i * m + j].add(i * m + j - 1);
                    }
                    if (i + 1 < n && free[i + 1][j]) {
                        graph[i * m + j].add(i * m + j + m);
                    }
                    if (i - 1 > -1 && free[i - 1][j]) {
                        graph[i * m + j].add(i * m + j - m);
                    }
                }
            }
        }
        for (int i = 0; i < n * m; i++) {
            System.out.println(Arrays.toString(graph[i].toArray()));
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new FileWriter("dominoes.out"));
        px = new int[n * m];
        py = new int[n * m];
        vis = new boolean[n * m];
        Arrays.fill(vis, false);
        Arrays.fill(px, -1);
        Arrays.fill(py, -1);
        boolean isPath = true;
        while (isPath) {
            isPath = false;
            Arrays.fill(vis, false);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if ((i + j) % 2 == 0) {
                        if (px[i * m + j] == -1) {
                            if (dfs(i * m + j)) {
                                isPath = true;
                            }
                        }
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < n * m; i++) {
            if (px[i] != -1) {
                count++;
            }
        }
        System.out.println(Math.min(count * a + b * (count_free - count * 2), count_free * b));
        out.write(String.valueOf(Math.min(count * a + b * (count_free - count * 2), count_free * b)));
        out.close();
    }

    private static boolean dfs(int i) {
        if (vis[i]) {
            return false;
        }
        vis[i] = true;
        for (int j : graph[i]) {
            if (py[j] == -1) {
                py[j] = i;
                px[i] = j;
                return true;
            } else {
                if (dfs(py[j])) {
                    py[j] = i;
                    px[i] = j;
                    return true;
                }
            }
        }
        return false;
    }
}