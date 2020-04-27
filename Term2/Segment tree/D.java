import java.io.*;

public class D {
    private static int x;
    private static int[][] t;
    private static int[][] b;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] split = reader.readLine().split("[\\s]");
        int n = Integer.valueOf(split[0]);
        int min = Integer.MAX_VALUE;
        b = new int[n][3];
        for (int i = 0; i < n; i++) {
            split = reader.readLine().split("[\\s]");
            b[i][0] = split[0].equals("W") ? 0 : 1;
            b[i][1] = Integer.parseInt(split[1]);
            b[i][2] = Integer.parseInt(split[2]);
            min = Math.min(min, b[i][1]);
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (b[i][1] < 0) {
                b[i][1] -= Math.min(0, min);
            } else {
                b[i][1] -= Math.min(-1, min);
            }
            b[i][2] += b[i][1];
            max = Math.max(b[i][2], max);
            b[i][2] -= 1;
        }
        x = 1;
        while (x < max) {
            x *= 2;
        }
        t = new int[8][2 * x - 1];
        for (int i = 0; i < 2 * x - 1; i++) {
            t[5][i] = Integer.MIN_VALUE;
            t[1][i] = i;
            t[2][i] = i;
            t[6][i] = 1;
        }
        for (int i = x - 2; i >= 0; i--) {
            t[1][i] = t[1][i * 2 + 1];
            t[2][i] = t[2][i * 2 + 2];
            t[6][i] = t[6][2 * i + 1] + t[6][2 * i + 2];
        }
        for (int i = 0; i < n; i++) {
            set(b[i][1] + x - 1, b[i][2] + x - 1, b[i][0], 0);
            System.out.println(t[7][0] + " " + t[5][0]);
        }
    }

    private static void set(int l, int r, int x, int v) {

        if ((t[2][v] < l) || (t[1][v] > r)) {
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                t[5][v] = x * t[6][v];
                if (x == 1) {
                    t[3][v] = t[1][v];
                    t[4][v] = t[2][v];
                } else {
                    t[3][v] = t[4][v] = 0;
                }
                t[7][v] = x;
            } else {
                if (t[5][v] == t[6][v]) {
                    t[5][2 * v + 1] = t[6][2 * v + 1];
                    t[5][2 * v + 2] = t[6][2 * v + 2];
                    t[7][2 * v + 1] = t[7][v];
                    t[7][2 * v + 2] = t[7][v];
                    t[3][2 * v + 1] = t[1][2 * v + 1];
                    t[4][2 * v + 1] = t[2][2 * v + 1];
                    t[3][2 * v + 2] = t[1][2 * v + 2];
                    t[4][2 * v + 2] = t[2][2 * v + 2];
                    t[5][v] = Integer.MIN_VALUE;
                }
                if (t[5][v] == 0) {
                    t[5][2 * v + 1] = 0;
                    t[5][2 * v + 2] = 0;
                    t[7][2 * v + 1] = 0;
                    t[7][2 * v + 2] = 0;
                    t[3][2 * v + 1] = 0;
                    t[4][2 * v + 1] = 0;
                    t[3][2 * v + 2] = 0;
                    t[4][2 * v + 2] = 0;
                    t[5][v] = Integer.MIN_VALUE;
                }
                set(l, r, x, 2 * v + 1);
                set(l, r, x, 2 * v + 2);
                t[5][v] = znach(t[5][2 * v + 1], t[5][2 * v + 2]);
                t[3][v] = t[3][2 * v + 1] != 0 ? t[3][2 * v + 1] : t[3][2 * v + 2];
                t[4][v] = t[4][2 * v + 2] != 0 ? t[4][2 * v + 2] : t[4][2 * v + 1];
                if (t[3][2 * v + 2] - t[4][2 * v + 1] == 1) {
                    t[7][v] = znach(t[7][2 * v + 1], t[7][2 * v + 2]) - 1;
                } else {
                    t[7][v] = znach(t[7][2 * v + 1], t[7][2 * v + 2]);
                }
            }
        }
    }


    private static int znach(int a, int b) {
        if ((a != Integer.MIN_VALUE) && (b != Integer.MIN_VALUE)) {
            return a + b;
        } else {
            if (a != Integer.MIN_VALUE) {
                return a;
            } else if (b != Integer.MIN_VALUE) {
                return b;
            }
        }
        return Integer.MIN_VALUE;
    }
}