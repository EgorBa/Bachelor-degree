import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class C {
    private static long[][] a;
    private static int n;
    private static int m;

    private static ArrayList<Integer> negativeCycle() {
        long[] d = new long[n];
        int[] p = new int[n];
        ArrayList<Integer> ans = new ArrayList<>();
        ArrayList<Integer> ans1 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            d[i] = 0;
            p[i] = -1;
        }
        int x = 0;
        for (int i = 0; i < n; i++) {
            x = -1;
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (a[u][v] == 100000) {
                        continue;
                    }
                    long w = a[u][v];
                    if (d[v] > d[u] + w) {
                        d[v] = Math.max(-1000000000, d[u] + w);
                        p[v] = u;
                        x = u;
                    }
                }
            }
        }
        if (x == -1) {
            return new ArrayList<>();
        } else {
            int y = x;
            for (int i = 0; i < n; ++i) {
                y = p[y];
            }
            for (int cur = y; ; cur = p[cur]) {
                ans.add(cur);
                if (cur == y && ans.size() > 1) {
                    break;
                }
            }
            for (int i = 0; i < ans.size() - 1; i++) {
                ans1.add(ans.get(ans.size() - 1 - i));
            }
            return ans1;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        a = new long[n][n];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            for (int j = 0; j < n; j++) {
                a[i][j] = Integer.parseInt(split[j]);
            }
        }
        ArrayList<Integer> ans = negativeCycle();
        if (ans.isEmpty()) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
            System.out.println(ans.size());
            for (Integer an : ans) {
                System.out.print(an + 1 + " ");
            }
        }
    }
}