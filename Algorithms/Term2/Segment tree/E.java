import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class E {
    public static int x;
    public static int[][] t;
    public static int res;
    public static int[] a = {1, 0, 0, 1};
    public static int[] b;
    public static int[] ans;
    public static int[] left;
    public static int[] right;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("crypto.in"));
        String[] split = reader.readLine().split("[\\s]");
        List<String> list = new ArrayList<>();
        res = Integer.valueOf(split[0]);
        int n = Integer.valueOf(split[1]);
        int m = Integer.valueOf(split[2]);
        x = 1;
        b = new int[4];
        ans = new int[4];
        while (x <= n) {
            x *= 2;
        }
        t = new int[6][2 * x - 1];
        for (int i = 0; i < n; i++) {
            split = reader.readLine().split("[\\s]");
            t[2][x - 1 + i] = Integer.parseInt(split[0]);
            t[3][x - 1 + i] = Integer.parseInt(split[1]);
            split = reader.readLine().split("[\\s]");
            t[4][x - 1 + i] = Integer.parseInt(split[0]);
            t[5][x - 1 + i] = Integer.parseInt(split[1]);
            reader.readLine();
        }
        for (int i = x - 1 + n; i < t[0].length; i++) {
            t[2][i] = a[0];
            t[3][i] = a[1];
            t[4][i] = a[2];
            t[5][i] = a[3];
        }
        for (int i = x - 2; i >= 0; i--) {
            t[2][i] = (t[2][2 * i + 1] * t[2][2 * i + 2] + t[3][2 * i + 1] * t[4][2 * i + 2]) % res;
            t[3][i] = (t[2][2 * i + 1] * t[3][2 * i + 2] + t[3][2 * i + 1] * t[5][2 * i + 2]) % res;
            t[4][i] = (t[4][2 * i + 1] * t[2][2 * i + 2] + t[5][2 * i + 1] * t[4][2 * i + 2]) % res;
            t[5][i] = (t[4][2 * i + 1] * t[3][2 * i + 2] + t[5][2 * i + 1] * t[5][2 * i + 2]) % res;
        }
        for (int i = x - 1; i < 2 * x - 1; i++) {
            t[0][i] = 1 + (i - x + 1);
            t[1][i] = 1 + (i - x + 1);
        }
        for (int i = x - 2; i >= 0; i--) {
            t[0][i] = t[0][2 * i + 1];
            t[1][i] = t[1][2 * i + 2];
        }
        for (int i = 0; i < m; i++) {
            split = reader.readLine().split("[\\s]");
            ans = rsq(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 0);
            list.add(ans[0] + " " + ans[1]);
            list.add(ans[2] + " " + ans[3]);
            list.add("");
        }
        reader.close();
        PrintWriter writer = new PrintWriter("crypto.out");
        for (int i = 0; i < list.size(); i++) {
            writer.println(list.get(i));
        }
        writer.close();
    }

    public static int[] rsq(int l, int r, int v) {
        if ((t[1][v] < l) || (t[0][v] > r)) {
            return a;
        } else {
            if ((t[0][v] >= l) && (t[1][v] <= r)) {
                for (int i = 0; i < 4; i++) {
                    b[i] = t[i + 2][v];
                }
                return b;
            } else {
                int[] le = new int[4];
                int[] re = new int[4];
                left = rsq(l, r, 2 * v + 1);
                for (int i = 0; i < 4; i++) {
                    le[i] = left[i];
                }
                right = rsq(l, r, 2 * v + 2);
                for (int i = 0; i < 4; i++) {
                    re[i] = right[i];
                }
                return multy(le, re);
            }

        }
    }

    public static int[] multy(int[] l, int[] r) {
        int[] ans = new int[4];
        ans[0] = (l[0] * r[0] + l[1] * r[2]) % res;
        ans[1] = (l[0] * r[1] + l[1] * r[3]) % res;
        ans[2] = (l[2] * r[0] + l[3] * r[2]) % res;
        ans[3] = (l[2] * r[1] + l[3] * r[3]) % res;
        return ans;
    }

}