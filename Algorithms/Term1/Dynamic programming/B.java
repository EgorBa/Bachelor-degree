import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class B {

    public static void main(String s[]) throws IOException {
        BufferedReader scan = new BufferedReader(new FileReader("input.txt"));
        String[] split = scan.readLine().split("[\\s]");
        int n = (int) Double.parseDouble(split[0]);
        int m = (int) Double.parseDouble(split[1]);
        File out = new File("output.txt");
        PrintWriter writer = new PrintWriter(out, "UTF8");
        int[][] a = new int[n][m];
        for (int i = 0; i < n; i++) {
            split = scan.readLine().split("[\\s]");
            for (int j = 0; j < m; j++) {
                a[i][j] = Integer.parseInt(split[j]);
            }
        }
        int[][] dp = new int[n][m];
        List<String> list = new ArrayList();
        int a1 = n - 1;
        int a2 = m - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) {
                    dp[i][j] = a[i][j];
                } else {
                    if (j == 0) {
                        dp[i][j] = dp[i - 1][j] + a[i][j];
                    } else {
                        if (i == 0) {
                            dp[i][j] = dp[i][j - 1] + a[i][j];
                        } else {
                            dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]) + a[i][j];
                        }
                    }
                }
            }
        }
        writer.println(dp[n - 1][m - 1]);
        while (a1 != 0 || a2 != 0) {
            if (a2 == 0) {
                list.add("D");
                a1--;
            } else {
                if (a1 == 0) {
                    list.add("R");
                    a2--;
                } else {
                    if (dp[a1][a2] - a[a1][a2] == dp[a1 - 1][a2]) {
                        list.add("D");
                        a1--;
                    } else {
                        list.add("R");
                        a2--;
                    }
                }
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            writer.print(list.get(i));
        }
        scan.close();
        writer.close();
    }
}