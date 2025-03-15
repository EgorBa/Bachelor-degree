import java.io.*;
import java.math.BigInteger;

public class C {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        BigInteger[] a = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            a[i] = new BigInteger(in.readLine());
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < n; i++) {
            out.write(a[i].isProbablePrime(8) ? "YES" : "NO");
            out.write('\n');
        }
        out.close();
    }
}
