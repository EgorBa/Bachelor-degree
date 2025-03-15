import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class M {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        HashMap<Double, Integer> map11 = new HashMap<>();
        HashMap<Double, Integer> map21 = new HashMap<>();
        double[] a = new double[n];
        double[] a1 = new double[n];
        double[] b = new double[n];
        double[] b1 = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            String[] split = in.readLine().split("[\\s]");
            a[i] = Integer.parseInt(split[0]);
            a1[i] = Integer.parseInt(split[0]);
            b[i] = Integer.parseInt(split[1]);
            b1[i] = Integer.parseInt(split[1]);
        }
        Arrays.sort(a1);
        Arrays.sort(b1);
        for (int i = 0; i < n; i++) {
            map11.put(a1[i], i + 1);
        }
        for (int i = 0; i < n; i++) {
            map21.put(b1[i], i + 1);
        }
        for (int i = 0; i < n; i++) {
            double n1 = map11.get(a[i]);
            double n2 = map21.get(b[i]);
            sum += Math.pow((n1 - n2), 2);
        }
        double ans = 1 - 6 * (sum / ((double) n * n * n - n));
        System.out.print(Double.isNaN(ans) ? 1 : ans);
    }
}