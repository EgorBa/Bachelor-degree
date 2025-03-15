import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class B {

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        String[] s = scan.readLine().split("[\\s]");
        int n = Integer.parseInt(s[0]);
        int[][] a = new int[2][n];
        int j = 0, count = 0;
        a[0][0] = Integer.parseInt(s[1]);
        a[1][0]++;
        for (int i = 2; i <= n; i++) {
            int t = Integer.parseInt(s[i]);
            if (a[0][j] == t) {
                a[1][j]++;
            }
            if (a[0][j] != t) {
                if (a[1][j] >= 3) {
                    count = count + a[1][j];
                    a[1][j] = 0;
                    if (a[0][j - 1] == t && j > 0) {
                        j--;
                        a[1][j]++;
                    } else {
                        a[0][j] = t;
                        a[1][j]++;
                    }
                } else {
                    j++;
                    a[0][j] = t;
                    a[1][j]++;
                }
            }
        }
        if (a[1][j] >= 3) {
            count += a[1][j];
        }
        System.out.print(count);
    }
}