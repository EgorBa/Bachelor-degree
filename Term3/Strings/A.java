import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class A {
    private static long m = 92375807L;
    public static int p = 29;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        int n = Integer.parseInt(in.readLine());
        ArrayList<String> ans = new ArrayList<>();
        long[] h = new long[str.length()];
        long[] pow = new long[str.length()];
        h[0] = str.charAt(0) - 'a' + 1;
        pow[0] = 1;
        for (int i = 1; i < str.length(); i++) {
            h[i] = ((h[i - 1] * p) % m + str.charAt(i) - 'a' + 1) % m;
            pow[i] = (pow[i - 1] * p) % m;
        }
        for (int i = 0; i < n; i++) {
            String[] split = in.readLine().split("[\\s]");
            int a1 = Integer.parseInt(split[0]) - 1;
            int a2 = Integer.parseInt(split[1]) - 1;
            int b1 = Integer.parseInt(split[2]) - 1;
            int b2 = Integer.parseInt(split[3]) - 1;
            if ((a1 == 0 ? h[a2] : (h[a2] + 2 * m - ((h[a1 - 1] * pow[a2 - a1 + 1]) % m)) % m) == (b1 == 0 ? h[b2] : (h[b2] + 2 * m - ((h[b1 - 1] * pow[b2 - b1 + 1]) % m)) % m)) {
                ans.add("Yes");
            } else {
                ans.add("No");
            }
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (String str1 : ans) {
            out.write(str1 + '\n');
        }
        out.close();
    }

}
