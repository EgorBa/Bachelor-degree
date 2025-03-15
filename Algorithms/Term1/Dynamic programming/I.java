import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class I {

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        String[] split = scan.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            String s = scan.readLine();
            a[i] = s;
        }
        System.out.println(count(a));
    }

    public static ArrayList<Integer>[] g;

    public static void dfs(int n, int S1, int S2) {
        if (n == 0) {
            g[S1].add(S2);
            return;
        }
        dfs(n - 1, S1 ^ (1 << (n - 1)), S2);
        dfs(n - 1, S1, S2 ^ (1 << (n - 1)));
        if (n >= 2) {
            dfs(n - 2, S1, S2);
        }
    }

    public static long count(String[] a) {
        int n = a.length, m = a[0].length();
        g = new ArrayList[1 << n];
        for (int i = 0; i < 1 << n; i++) {
            g[i] = new ArrayList<>();
        }
        dfs(n, 0, 0);
        long[][] dp = new long[m + 1][1 << n];
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            int mask = 0;
            for (int j = 0; j < n; j++) {
                if (a[j].charAt(i) == 'X') {
                    mask ^= (1 << j);
                }
            }
            for (int S = 0; S < 1 << n; S++) {
                if ((S & mask) == 0) {
                    for (int x : g[S | mask]) {
                        dp[i + 1][x] += dp[i][S];
                    }
                }
            }
        }
        return dp[m][0];
    }
}