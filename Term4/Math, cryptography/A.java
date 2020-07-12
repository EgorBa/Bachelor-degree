import java.io.*;

public class A {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        int max = Integer.MIN_VALUE;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(in.readLine());
            max = Math.max(a[i], max);
        }
        boolean[] r = new boolean[max + 1];
        for (int i = 2; i <= max; i++) {
            if (r[i]) continue;
            long bf = (long) i * i;
            while (bf <= max) {
                r[(int) bf] = true;
                bf += i;
            }
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < n; i++) {
            if (r[a[i]]) {
                out.write("NO");
            } else {
                out.write("YES");
            }
            out.write('\n');
        }
        out.close();
    }
}