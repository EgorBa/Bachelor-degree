import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class K {
    public static int x;
    public static int[][] t;
    public static int j;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("parking.in"));
        String[] split = reader.readLine().split("[\\s]");
        List list = new ArrayList();
        int n = Integer.valueOf(split[0]);
        int m = Integer.valueOf(split[1]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = 1;
        }
        x = 1;
        while (x <= a.length) {
            x *= 2;
        }
        t = new int[3][2 * x - 1];
        for (int i = 0; i < n; i++) {
            t[0][x - 1 + i] = a[i];
        }
        for (int i = x - 2; i >= 0; i--) {
            t[0][i] = t[0][2 * i + 1] + t[0][2 * i + 2];
        }
        for (int i = x - 1; i < 2 * x - 1; i++) {
            t[1][i] = 1 + (i - x + 1);
            t[2][i] = 1 + (i - x + 1);
        }
        for (int i = x - 2; i >= 0; i--) {
            t[1][i] = t[1][2 * i + 1];
            t[2][i] = t[2][2 * i + 2];
        }
        for (int i = 0; i < m; i++) {
            split = reader.readLine().split("[\\s]");
            if (split[0].equals("enter")) {
                int y = Integer.valueOf(split[1]);
                if (a[y - 1] == 1) {
                    list.add(y);
                    a[y - 1] = 0;
                    update(y, 0);
                } else {
                    int r = rsq(y, n, 0);
                    if (r == 0) {
                        j = 0;
                        find(1);
                        j -= x - 2;
                        list.add(j);
                        a[j - 1] = 0;
                        update(j, 0);
                    } else {
                        j = 0;
                        int k = rsq(1, y, 0) + 1;
                        find(k);
                        j -= x - 2;
                        list.add(j);
                        a[j - 1] = 0;
                        update(j, 0);
                    }
                }
            } else {
                int y = Integer.valueOf(split[1]);
                a[y - 1] = 1;
                update(y, 1);
            }
        }
        reader.close();
        PrintWriter writer = new PrintWriter("parking.out");
        for (int i = 0; i < list.size(); i++) {
            writer.println(list.get(i));
        }
        writer.close();
    }

    public static void update(int y, int u) {
        t[0][y - 2 + x] = u;
        y = y - 2 + x;
        while (y > 0) {
            y = (y - 1) / 2;
            t[0][y] = t[0][2 * y + 1] + t[0][2 * y + 2];
        }
    }

    public static void find(int k) {
        while (2 * j + 1 < t[0].length) {
            if (k <= t[0][2 * j + 1]) {
                j = 2 * j + 1;
            } else {
                j = 2 * j + 2;
                k = k - t[0][j - 1];
            }
        }
    }

    public static int rsq(int l, int r, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
            return 0;
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                return t[0][v];
            } else {
                return rsq(l, r, 2 * v + 1) + rsq(l, r, 2 * v + 2);
            }

        }
    }

}