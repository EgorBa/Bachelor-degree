import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Stack;

public class H {
    private static final double INF = Double.POSITIVE_INFINITY;

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(scan.readLine());
        int weight[][] = new int[n][n];
        double dp[][] = new double[1 << (n)][n];
        int[] order = new int[n];

        for (int i = 0; i < n; i++) {
            String[] split = scan.readLine().split("[\\s]");
            for (int j = 0; j < n; j++) {
                weight[i][j] = Integer.parseInt(split[j]);
            }
        }
        for (int i = 0; i < 1 << n; i++)
            for (int j = 0; j < n; j++)
                dp[i][j] = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++)
            dp[1 << i][i] = 0;
        for (int i = 1; i < 1 << n; i++)
            for (int j = 0; j < n; j++)
                if ((i & 1 << j) != 0)
                    for (int k = 0; k < n; k++)
                        if ((i & 1 << k) == 0)
                            dp[i ^ 1 << k][k] = Math.min(dp[i ^ 1 << k][k], dp[i][j] + weight[j][k]);
        double minDist = INF;
        for (int i = 0; i < n; i++)
            minDist = Math.min(minDist, (int) dp[(1 << n) - 1][i]);
        System.out.println((int) minDist);
        int currState = (1 << n) - 1;
        int last = -1;
        for (int i = n - 1; i >= 0; i--) {
            int next = -1;
            for (int j = 0; j < n; j++)
                if ((currState & 1 << j) != 0 && (next == -1 || dp[currState][j] + (last == -1 ? 0 : weight[j][last]) < dp[currState][next] + (last == -1 ? 0 : weight[next][last])))
                    next = j;
            order[i] = last = next;
            currState ^= 1 << last;
        }
        for (int i = 0; i < n; i++) {
            System.out.print(order[i] + 1 + " ");
        }
    }

}