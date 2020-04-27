import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class F {
    public static final double INF = Double.POSITIVE_INFINITY;

    public static void main(String s[]) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        String[] split = scan.readLine().split("[\\s]");
        int n = (int) Double.parseDouble(split[0]);
        if (n == 0) {
            System.out.println(0);
            System.out.println(0 + " " + 0);
        } else {
            long[] a = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(scan.readLine());
            }
            double[][] money = new double[n + 1][n + 2];
            for (int i = 0; i < n + 1; i++) {
                for (int j = 0; j < n + 2; j++) {
                    money[i][j] = INF;
                }
            }
            if (a[0] <= 100) {
                money[1][1] = a[0];
            } else {
                money[1][1] = money[1][2] = a[0];
            }
            for (int i = 2; i <= n; i++) {
                for (int j = 1; j < n + 1; j++) {
                    if (a[i - 1] <= 100) {
                        money[i][j] = Math.min(money[i - 1][j] + a[i - 1], money[i - 1][j + 1]);
                    } else {
                        money[i][j] = Math.min(money[i - 1][j + 1], Math.min(money[i - 1][j - 1] + a[i - 1], money[i - 1][j] + a[i - 1]));
                    }
                }
            }
            System.out.println((int) money[n][1]);
            int count = 0;
            for (int i = 2; i <= n + 1; i++) {
                if (money[n][1] == money[n][i]) {
                    count++;
                } else {
                    break;
                }
            }
            int p = 1;
            List list = new ArrayList();
            for (int i = n - 1; i > 0; i--) {
                for (int j = p - 1; j < n; j++) {
                    if (money[n][p] == money[i][j] + a[n - 1]) {
                        n = i;
                        p = j;
                        break;
                    }
                    if (money[n][p] == money[i][j]) {
                        list.add(n);
                        n = i;
                        p = j;
                        break;
                    }
                }
            }
            System.out.println(count + " " + list.size());
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        }
    }
}