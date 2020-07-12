import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class E {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(in.readLine());
        long e = Long.parseLong(in.readLine());
        long c = Long.parseLong(in.readLine());
        long phi = phi(n);
        BigInteger e1 = new BigInteger(String.valueOf(e));
        BigInteger d = e1.modInverse(new BigInteger(String.valueOf(phi)));
        long m = step(c, Long.parseLong(d.toString()), n);
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        out.write(String.valueOf(m));
        out.close();
    }

    private static long step(long m, long e, long n) {
        if (e == 1) {
            return m;
        }
        if (e % 2 == 0) {
            long ans = step(m, e / 2, n);
            return (ans * ans) % n;
        } else {
            return (m * step(m, e - 1, n)) % n;
        }
    }

    private static long phi(long n) {
        long result = n;
        for (int i = 2; i * i <= n; ++i)
            if (n % i == 0) {
                while (n % i == 0) {
                    n /= i;
                }
                result -= result / i;
            }
        if (n > 1)
            result -= result / n;
        return result;
    }
}