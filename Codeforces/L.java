import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class L {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        double avgA = 0;
        double avgB = 0;
        double[] a = new double[n];
        double[] b = new double[n];
        double a1 = 0;
        double a2 = 0;
        double a3 = 0;
        for (int i = 0; i < n; i++) {
            String[] split = in.readLine().split("[\\s]");
            a[i] = Integer.parseInt(split[0]);
            b[i] = Integer.parseInt(split[1]);
            avgA += a[i];
            avgB += b[i];
        }
        avgA /= n;
        avgB /= n;
        for (int i = 0; i < n; i++) {
            a[i] -= avgA;
            b[i] -= avgB;
        }
        for (int i = 0; i < n; i++) {
            a1 += a[i] * b[i];
            a2 += a[i] * a[i];
            a3 += b[i] * b[i];
        }
        double ans = a1 / Math.sqrt(a2 * a3);
        System.out.print(Double.isNaN(ans) ? 0 : ans);
    }
}