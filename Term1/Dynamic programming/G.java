import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class G {
    static int n;
    static String s;

    static void posled(int l, int r, int[][] dp, int[][] ep) {
        if (dp[l][r] == r - l + 1)
            return;
        if (dp[l][r] == 0) {
            System.out.print(s.substring(l, r - l + 1 + l));
            return;
        }
        if (ep[l][r] == -1) {
            System.out.print(s.charAt(l));
            posled(l + 1, r - 1, dp, ep);
            System.out.print(s.charAt(r));
            return;
        }
        posled(l, ep[l][r], dp, ep);
        posled(ep[l][r] + 1, r, dp, ep);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        s = scan.readLine();
        n = s.length();
        int[][] dp = new int[n][n];
        int[][] ep = new int[n][n];
        for (int r = 0; r < n; ++r)
            for (int l = r; l >= 0; --l) {
                if (l == r)
                    dp[l][r] = 1;
                else {
                    int best = 1000000;
                    int mk = -1;
                    if (s.charAt(l) == '(' && s.charAt(r) == ')' || s.charAt(l) == '[' &&
                            s.charAt(r) == ']' || s.charAt(l) == '{' && s.charAt(r) == '}')
                        best = dp[l + 1][r - 1];
                    for (int k = l; k < r; ++k)
                        if (dp[l][k] + dp[k + 1][r] < best) {
                            best = dp[l][k] + dp[k + 1][r];
                            mk = k;
                        }
                    dp[l][r] = best;
                    ep[l][r] = mk;
                }
            }
        posled(0, n - 1, dp, ep);
    }
}