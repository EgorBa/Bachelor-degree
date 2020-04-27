import java.io.*;

public class J {

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new FileReader("nice.in"));
        String[] split = scan.readLine().split("[\\s]");
        int first = Integer.parseInt(split[0]);
        int second = Integer.parseInt(split[1]);
        File out = new File("nice.out");
        PrintWriter writer = new PrintWriter(out, "UTF8");
        int m = Math.min(first, second);
        int n = Math.max(first, second);
        int x = 1;
        for (int i = 1; i <= m; i++) {
            x <<= 1;
        }
        int dp[][] = new int[n + 1][x + 1];
        for (int i = 0; i < x; i++) {
            dp[1][i] = 1;
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < x; j++) {
                for (int k = 0; k < x; k++) {
                    if (check(j, k, m)) {
                        dp[i][k] = dp[i - 1][j] + dp[i][k];
                    }
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < x; i++) {
            ans += dp[n][i];
        }
        writer.println(ans);
        writer.close();
    }

    private static boolean check(int x, int y, int m) {
        int a[] = new int[m + 1];
        int b[] = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            a[i] = x & 1;
            b[i] = y & 1;
            x >>= 1;
            y >>= 1;
            if ((i > 1) && (a[i] == a[i - 1]) && (a[i] == b[i]) && (a[i] == b[i - 1]))
                return false;
        }
        return true;
    }
}