import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class E {

    private static class UFO {
        int x;
        int y;
        int time;

        UFO(int a, int b, int c) {
            x = a;
            y = b;
            time = c;
        }
    }

    private static ArrayList<Integer>[] graph;
    private static ArrayList<Integer>[] graph2;
    private static ArrayList<Integer>[] graph3;
    private static int[] px;
    private static int[] py;
    private static boolean[] vis;
    private static int v;
    private static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("ufo.in"));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        v = Integer.parseInt(split[1]);
        graph = new ArrayList[n];
        graph2 = new ArrayList[2 * n];
        graph3 = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            graph3[i] = new ArrayList<>();
        }
        for (int i = 0; i < 2 * n; i++) {
            graph2[i] = new ArrayList<>();
        }
        UFO[] time = new UFO[n];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            String[] str = split[0].split(":");
            time[i] = new UFO(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]));
        }
        Arrays.sort(time, new Comparator<UFO>() {
            @Override
            public int compare(UFO e1, UFO e2) {
                return Integer.compare(e1.time, e2.time);
            }
        });
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((time[j].time - time[i].time) >= (Math.sqrt((time[i].x - time[j].x) * (time[i].x - time[j].x) + (time[i].y - time[j].y) * (time[i].y - time[j].y)) / v) * 60) {
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
        vis = new boolean[2 * n];
        Arrays.fill(vis, false);
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
        PrintWriter out = new PrintWriter("ufo.out");
        out.println(count);
        System.out.println(count);
        out.close();
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