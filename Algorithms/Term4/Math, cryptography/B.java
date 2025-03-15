import java.io.*;

public class B {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        int max = Integer.MIN_VALUE;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(in.readLine());
            max = Math.max(a[i], max);
        }
        int[] r = new int[max + 1];
        for (int i = 2; i <= max; i++) {
            if (r[i] != 0) continue;
            r[i] = i;
            long bf = (long) i * i;
            while (bf <= max) {
                if (r[(int) bf] == 0) {
                    r[(int) bf] = i;
                }
                bf += i;
            }
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < n; i++) {
            int b = a[i];
            while (r[b] != b) {
                out.write(r[b] + " ");
                b = b / r[b];
            }
            out.write(b + " ");
            out.write('\n');
        }
        out.close();
    }
}