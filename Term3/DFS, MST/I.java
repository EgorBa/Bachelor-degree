import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class I {
    private static int n = 0;
    private static double[] weights;
    private static int[] x;
    private static int[] y;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        y = new int[n];
        x = new int[n];
        double mst = 0;
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            x[i] = Integer.parseInt(split[0]);
            y[i] = Integer.parseInt(split[1]);
            weights[i] = 1000000;
        }
        weights[0] = 0;
        int j = 0;
        for (int h = 0; h < n - 1; h++) {
            for (int i = 0; i < n; i++) {
                if (weights[i] == 0) {
                    continue;
                }
                weights[i] = Math.min(weights[i], Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])));
            }
            double min = 1000000;
            for (int k = 0; k < n; k++) {
                if (weights[k] != 0 && weights[k] < min) {
                    min = weights[k];
                    j = k;
                }
            }
            mst += min;
        }
        System.out.println(mst);
    }
}