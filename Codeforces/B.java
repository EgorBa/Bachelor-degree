import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

public class B {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int k = Integer.parseInt(in.readLine());
        double[][] a = new double[k + 1][k + 1];
        double all = 0;
        for (int i = 0; i < k; i++) {
            String[] strs = in.readLine().split("[\\s]");
            for (int j = 0; j < k; j++) {
                a[i][j] = Integer.parseInt(strs[j]);
                a[i][k] += a[i][j];
                a[k][j] += a[i][j];
                all += a[i][j];
            }
        }
        double micro = 0;
        double macroPrecision = 0;
        double macroRecall = 0;
        for (int i = 0; i < k; i++) {
            double p = 0;
            double r = 0;
            if (a[i][k] != 0) {
                p = a[i][i] / a[i][k];
            }
            if (a[k][i] != 0) {
                r = a[i][i] / a[k][i];
            }
            if (a[i][i] != 0) {
                micro += (2 * p * r * (a[i][k] / all) / (p + r));
            }
            macroRecall += r * (a[i][k] / all);
            macroPrecision += p * (a[i][k] / all);
        }
        PrintWriter out = new PrintWriter(System.out);
        if (macroPrecision == macroRecall && macroRecall == 0) {
            out.println(0);
        } else {
            out.println((2 * macroPrecision * macroRecall) / (macroRecall + macroPrecision));
        }
        out.println(micro);
        out.close();
    }
}