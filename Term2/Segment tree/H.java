import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class H {
    public static int x;
    public static int[][] t;
    public static int m;
    public static int[][] b;
    public static final int CONST = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("rmq.in"));
        String[] split = reader.readLine().split("[\\s]");
        int n = Integer.valueOf(split[0]);
        m = Integer.valueOf(split[1]);
        x = 1;
        b = new int[3][m];
        while (x <= n) {
            x *= 2;
        }
        t = new int[4][2 * x - 1];
        for (int i = 0; i < t[0].length; i++) {
            t[0][i] = Integer.MAX_VALUE;
            t[3][i] = CONST;
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
            b[0][i] = Integer.parseInt(split[0]);
            b[1][i] = Integer.parseInt(split[1]);
            b[2][i] = Integer.parseInt(split[2]);
        }
        quickSort(b[2], 0, m - 1);
        for (int i = 0; i < m; i++) {
            set(b[0][i], b[1][i], b[2][i], 0);
        }
        reader.close();
        bigpush();
        PrintWriter writer = new PrintWriter("rmq.out");
        if (check()) {
            writer.println("consistent");
            for (int i = 0; i < n; i++) {
                writer.print(znach(x - 1 + i) + " ");
            }
        } else {
            writer.println("inconsistent");
        }
        writer.close();
    }

    private static void set(int l, int r, int x, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                t[3][v] = x;
            } else {
                if (t[3][v] != CONST) {
                    t[3][2 * v + 1] = t[3][v];
                    t[3][2 * v + 2] = t[3][v];
                    t[3][v] = CONST;
                }
                set(l, r, x, 2 * v + 1);
                set(l, r, x, 2 * v + 2);
                t[0][v] = Math.min(znach(2 * v + 1), znach(2 * v + 2));
            }
        }
    }

    private static void bigpush() {
        for (int i = 0; i < x - 1; i++) {
            if (t[3][i] != CONST) {
                t[0][i] = t[3][i];
                t[3][2 * i + 1] = t[3][i];
                t[3][2 * i + 2] = t[3][i];
                t[3][i] = CONST;
            }
        }
    }

    private static int znach(int v) {
        if (t[3][v] != CONST) {
            return t[3][v];
        } else {
            return t[0][v];
        }
    }


    public static boolean check() {
        boolean result = true;
        for (int i = 0; i < m; i++) {
            if (b[2][i] != rsq(b[0][i], b[1][i], 0)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static int rsq(int l, int r, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
            return Integer.MAX_VALUE;
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                return znach(v);
            } else {
                t[0][v] = znach(v);
                if (t[3][v] != CONST) {
                    t[3][2 * v + 1] = t[3][v];
                    t[3][2 * v + 2] = t[3][v];
                    t[3][v] = CONST;
                }
                return Math.min(rsq(l, r, 2 * v + 1), rsq(l, r, 2 * v + 2));

            }
        }
    }

    public static void quickSort(int[] array, int low, int high) {
        if (array.length == 0)
            return;
        if (low >= high)
            return;
        int m = low + (high - low) / 2;
        int pivot = array[m];
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                for (int k = 0; k < 2; k++) {
                    int temp = b[k][i];
                    b[k][i] = b[k][j];
                    b[k][j] = temp;
                }
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(array, low, j);
        if (high > i)
            quickSort(array, i, high);
    }

}