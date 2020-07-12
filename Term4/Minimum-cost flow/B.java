import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class B {

    private static int n;

    public static void main (String[]args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(in.readLine());
        double[][] weight = new double[n + 1][n + 1];
        for (double[] doubles : weight) {
            Arrays.fill(doubles, Integer.MAX_VALUE);
        }
        for (int i = 1; i <= n; i++) {
            String[] split = in.readLine().split("[\\s]");
            for (int j = 1; j <= n; j++) {
                int x = Integer.parseInt(split[j - 1]);
                weight[i][j] = x;
            }
        }
        double[] u = new double[n + 1];
        double[] v = new double[n + 1];
        int[] p = new int[n + 1];
        int[] path = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            p[0] = i;
            int k = 0;
            double[] minValues = new double[n + 1];
            Arrays.fill(minValues, Double.POSITIVE_INFINITY);
            boolean[] used = new boolean[n + 1];
            Arrays.fill(used,false);
            do {
                used[k] = true;
                int t = p[k];
                double delta = Double.POSITIVE_INFINITY;
                int e = 0;
                for (int j = 1; j <= n; j++) {
                    if (!used[j]) {
                        double cur = weight[t][j] - u[t] - v[j];
                        if (cur < minValues[j]) {
                            minValues[j] = cur;
                            path[j] = k;
                        }
                        if (minValues[j] < delta) {
                            delta = minValues[j];
                            e = j;
                        }
                    }
                }
                for (int j = 0; j <= n; j++) {
                    if (used[j]) {
                        u[p[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minValues[j] -= delta;
                    }
                }
                k = e;
            } while (p[k] != 0);
            do {
                int j1 = path[k];
                p[k] = p[j1];
                k = j1;
            } while (k != 0);
        }
        int[] ans = new int[n + 1];
        for (int j = 1; j <= n; ++j) {
            ans[p[j]] = j;
        }
        double c = 0;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            c += weight[i][ans[i]];
            list.add((i) + " " + (ans[i]));
        }
        System.out.println((long)c);
        list.forEach(System.out::println);
    }
}