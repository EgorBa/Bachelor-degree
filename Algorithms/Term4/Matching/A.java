import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class A {

    private static ArrayList<Integer>[] graph;
    private static int[] px ;
    private static int[] py ;
    private static boolean[] vis ;
    private static int m;
    private static int n;

    public static void main (String[]args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        graph = new ArrayList[n + m];
        for (int i = 0; i < n + m; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            for (int j = 0; j < split.length - 1; j++) {
                graph[i].add(Integer.parseInt(split[j]) - 1 + n);
                graph[Integer.parseInt(split[j]) - 1 + n].add(i);
            }
        }
        px = new int[n];
        py = new int[m];
        vis = new boolean[n + m];
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
            if (px[i] >= n) {
                count++;
            }
        }
        System.out.println(count);
        for (int i = 0; i < n; i++) {
            if (px[i] >= n) {
                System.out.println((i + 1) + " " + (px[i] - n + 1));
            }
        }
    }

    private static boolean dfs(int i) {
        if (vis[i]) {
            return false;
        }
        vis[i] = true;
        for (int j : graph[i]) {
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