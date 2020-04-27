import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class F {
    private static int[] a;
    private static int[][] t;
    private static int[] log;
    private static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] split = reader.readLine().split("[\\s]");
        n = Integer.valueOf(split[0]);
        int m = Integer.valueOf(split[1]);
        int y = Integer.valueOf(split[2]);
        a = new int[Integer.valueOf(split[0])];
        t = new int[n][fl(n) + 1];
        a[0] = y;
        t[0][0] = a[0];
        for (int i = 1; i < n; i++) {
            a[i] = (23 * a[i - 1] + 21563) % (16714589);
            t[i][0] = a[i];
        }
        for (int j = 1; j <= fl(n) + 1; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                t[i][j] = Math.min(t[i][j - 1], t[i + (1 << (j - 1))][j - 1]);
            }
        }
        buildLog();
        split = reader.readLine().split("[\\s]");
        int u = Integer.valueOf(split[0]);
        int v = Integer.valueOf(split[1]);
        for (int i = 1; i <= m; i++) {
            int l = Math.min(u, v);
            int r = Math.max(u, v);
            int ans = getmin(l - 1, r - 1);
            if (i == m) {
                System.out.println(u + " " + v + " " + ans);
            }
            u = ((17 * u + 751 + ans + 2 * (i)) % n) + 1;
            v = ((13 * v + 593 + ans + 5 * (i)) % n) + 1;
        }
    }

    private static int getmin(int l, int r) {
        int k = log[r - l + 1];
        return Math.min(t[l][k], t[r - (1 << k) + 1][k]);
    }

    private static int fl(int len) {
        if (len == 1) {
            return 0;
        } else {
            return fl(len / 2) + 1;
        }
    }

    private static void buildLog() {
        log = new int[n + 1];
        log[1] = 0;
        for (int i = 2; i < n + 1; i++) {
            log[i] = log[i >> 1] + 1;
        }
    }
}
