import java.io.*;
import java.math.BigInteger;

public class D {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("\\s+");
        int a = Integer.parseInt(split[0]);
        int b = Integer.parseInt(split[1]);
        int n = Integer.parseInt(split[2]);
        int m = Integer.parseInt(split[3]);
        long x = Math.min(a, b);
        int go = x == b ? m : n;
        while (((x % n) != a) || ((x % m) != b)) {
            x += go;
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        out.write(String.valueOf(x));
        out.close();
    }
}
