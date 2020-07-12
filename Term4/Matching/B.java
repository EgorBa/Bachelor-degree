import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class B {

    private static ArrayList<Integer>[] graph;
    private static ArrayList<Integer>[] graph2;
    private static ArrayList<Integer>[] graph3;
    private static int[] px ;
    private static int[] py ;
    private static boolean[] vis ;
    private static int n;

    public static void main (String[]args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("taxi.in"));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        graph = new ArrayList[n];
        graph2 = new ArrayList[2*n];
        graph3 = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            graph3[i] = new ArrayList<>();
        }
        for (int i = 0; i < 2*n; i++) {
            graph2[i] = new ArrayList<>();
        }
        int[][] time = new int[n][5];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            time[i][1] = Integer.parseInt(split[1]);
            time[i][2] = Integer.parseInt(split[2]);
            time[i][3] = Integer.parseInt(split[3]);
            time[i][4] = Integer.parseInt(split[4]);
            String[] str = split[0].split(":");
            time[i][0] = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (time[i][0] + Math.abs(time[i][1] - time[i][3]) + Math.abs(time[i][2] - time[i][4]) + Math.abs(time[j][1] - time[i][3]) + Math.abs(time[j][2] - time[i][4]) < time[j][0]) {
                    graph[i].add(j);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j : graph[i]) {
                graph2[i].add(j + n);
            }
        }
        px = new int[n];
        py = new int[n];
        vis = new boolean[2*n];
        Arrays.fill(vis,false);
        Arrays.fill(px, -1);
        Arrays.fill(py, -1);
        boolean isPath = true;
        while (isPath) {
            isPath = false;
            Arrays.fill(vis, false);
            for (int i = 0; i < n; i++) {
                if (px[i] == -1) {
                    if (dfs(i)) {
                        isPath = true;
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (px[i] != -1) {
                graph3[i].add(px[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (graph3[i].isEmpty()) {
                count++;
            }
        }
        in.close();
        PrintWriter out = new PrintWriter("taxi.out");
        out.println(count);
        out.close();
        System.out.println(count);
    }

    private static boolean dfs(int i) {
        if (vis[i]) {
            return false;
        }
        vis[i] = true;
        for (int j : graph2[i]) {
            if (py[j - n] == -1) {
                py[j - n] = i;
                px[i] = j;
                return true;
            } else {
                if (dfs(py[j - n])) {
                    py[j - n] = i;
                    px[i] = j;
                    return true;
                }
            }
        }
        return false;
    }
}