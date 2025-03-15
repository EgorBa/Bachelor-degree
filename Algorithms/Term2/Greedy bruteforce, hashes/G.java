import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;

public class G {
    private static int[] ch;
    private static int u = 0;
    private static int count = 0;

    private static void prem(int[] list3, int y, int n, boolean[] set) {
        if (n == 0) {
            int r = 0;
            for (int j = 0; j < list3.length - 1; j++) {
                r += (list3[j] + 1) * (list3[j + 1] + 1);
            }
            r %= u;
            if (r == 0 || ch[r] % 3 == 0) {
                count++;
            }
            return;
        }
        n--;
        for (int i = 0; i < set.length; i++) {
            if (!set[i]) {
                set[i] = true;
                list3[y] = i;
                prem(list3, y + 1, n, set);
                set[i] = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("beautiful.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        u = Integer.parseInt(split[1]);
        boolean[] set = new boolean[n];
        ch = new int[u];
        for (int i = 0; i < u; i++) {
            ch[i] = check(i);
        }
        prem(new int[n], 0, n, set);
        in.close();
        PrintWriter out = new PrintWriter("beautiful.out");
        out.println(count);
        out.close();
    }

    private static int check(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int limit = (int) Math.sqrt(n);
        int count = 2;
        for (int i = 2; i <= limit; i++) {
            if (n % i == 0) {
                count++;
                if (n / i != i) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean IsSimple(double ANum) {
        if (ANum < 2)
            return false;
        double s = Math.sqrt(ANum);
        for (int i = 2; i <= s; i++) {
            if (ANum % i == 0)
                return false;
        }
        return true;
    }

}
