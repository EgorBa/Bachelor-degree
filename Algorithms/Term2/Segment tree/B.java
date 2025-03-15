import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class B {
    public static long[] a;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.valueOf(reader.readLine());
        String[] split = reader.readLine().split("[\\s]");
        a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.valueOf(split[i]);
        }
        long[] t = new long[n];
        for (int i = 0; i < n; i++) {
            update1(i, a[i], t);
        }
        while (reader.ready()) {
            split = reader.readLine().split("[\\s]");
            if (split[0].equals("sum")) {
                System.out.println(sum(Integer.valueOf(split[2]) - 1, t) - sum(Integer.valueOf(split[1]) - 2, t));
            } else {
                update2(Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]), t);
            }
        }
    }

    public static void update2(int i, long x, long[] t) {
        long y = -a[i] + x;
        a[i] = x;
        while (i < t.length) {
            t[i] += y;
            i = i | (i + 1);
        }
    }

    public static void update1(int i, long x, long[] t) {
        while (i < t.length) {
            t[i] += x;
            i = i | (i + 1);
        }
    }

    public static long sum(int i, long[] t) {
        long res = 0;
        while (i >= 0) {
            res += t[i];
            i = (i & (i + 1)) - 1;
        }
        return res;
    }
}