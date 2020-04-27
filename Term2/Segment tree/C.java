import javax.naming.NameNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class C {
    public static int x;
    public static long[][] t;
    public static final int CONST = Integer.MAX_VALUE - 12;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] split = reader.readLine().split("[\\s]");
        int n = Integer.valueOf(split[0]);
        int[] a = new int[n];
        split = reader.readLine().split("[\\s]");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.valueOf(split[i]);
        }
        x = 1;
        while (x <= a.length) {
            x *= 2;
        }
        t = new long[6][2 * x - 1];
        for (int i = 0; i < n; i++) {
            t[0][x - 1 + i] = a[i];
        }
        for (int i = n + x - 1; i < 2 * x - 1; i++) {
            t[0][i] = Long.MAX_VALUE;
        }
        for (int i = x - 2; i >= 0; i--) {
            t[0][i] = Math.min(t[0][2 * i + 1], t[0][2 * i + 2]);
        }

        for (int i = 0; i < 2 * x - 1; i++) {
            t[4][i] = CONST;
            t[5][i] = 3;
        }

        for (int i = x - 1; i < 2 * x - 1; i++) {
            t[1][i] = 1 + (i - x + 1);
            t[2][i] = 1 + (i - x + 1);
        }
        for (int i = x - 2; i >= 0; i--) {
            t[1][i] = t[1][2 * i + 1];
            t[2][i] = t[2][2 * i + 2];
        }
        while (reader.ready()) {
            split = reader.readLine().split("[\\s]");
            if (split[0].equals("min")) {
                System.out.println(rsq(Integer.valueOf(split[1]), Integer.valueOf(split[2]), 0));
            } else {
                if (split[0].equals("add")) {
                    add(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Long.valueOf(split[3]), 0);
                } else {
                    set(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Long.valueOf(split[3]), 0);
                }
            }
        }
    }

    private static long znach(int v) {
        if (t[5][v] != 4) {
            if (t[4][v] != CONST) {
                return t[4][v] + t[3][v];
            } else {
                return t[0][v] + t[3][v];
            }
        } else {
            return t[4][v];
        }
    }

    private static void add(int l, int r, long x, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                t[3][v] += x;
                t[5][v] = 3;
            } else {
                if (t[5][v] != 4) {
                    if (t[4][v] != CONST) {
                        t[3][2 * v + 1] = t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] = t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[4][2 * v + 1] = t[4][v];
                        t[4][2 * v + 2] = t[4][v];
                        t[4][v] = CONST;
                        t[3][v] = 0;
                    } else {
                        t[3][2 * v + 1] += t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] += t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[3][v] = 0;
                    }
                } else {
                    t[4][2 * v + 1] = t[4][v];
                    t[3][2 * v + 1] = 0;
                    t[5][2 * v + 1] = 4;
                    t[4][2 * v + 2] = t[4][v];
                    t[3][2 * v + 2] = 0;
                    t[5][2 * v + 2] = 4;
                    t[4][v] = CONST;
                    t[3][v] = 0;
                    t[5][v] = 3;
                }
                add(l, r, x, 2 * v + 1);
                add(l, r, x, 2 * v + 2);
                t[0][v] = Math.min(znach(2 * v + 1), znach(2 * v + 2));
            }
        }
    }

    private static void set(int l, int r, long x, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                t[3][v] = 0;
                t[4][v] = x;
                t[5][v] = 4;
            } else {
                if (t[5][v] != 4) {
                    if (t[4][v] != CONST) {
                        t[3][2 * v + 1] = t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] = t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[4][2 * v + 1] = t[4][v];
                        t[4][2 * v + 2] = t[4][v];
                        t[4][v] = CONST;
                        t[3][v] = 0;
                    } else {
                        t[3][2 * v + 1] += t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] += t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[3][v] = 0;
                    }
                } else {
                    t[4][2 * v + 1] = t[4][v];
                    t[3][2 * v + 1] = 0;
                    t[5][2 * v + 1] = 4;
                    t[4][2 * v + 2] = t[4][v];
                    t[3][2 * v + 2] = 0;
                    t[5][2 * v + 2] = 4;
                    t[4][v] = CONST;
                    t[3][v] = 0;
                    t[5][v] = 3;
                }
                set(l, r, x, 2 * v + 1);
                set(l, r, x, 2 * v + 2);
                t[0][v] = Math.min(znach(2 * v + 1), znach(2 * v + 2));
            }
        }
    }


    public static long rsq(int l, int r, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
            return Long.MAX_VALUE;
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                return znach(v);
            } else {
                t[0][v] = znach(v);
                if (t[5][v] != 4) {
                    if (t[4][v] != CONST) {
                        t[3][2 * v + 1] = t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] = t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[4][2 * v + 1] = t[4][v];
                        t[4][2 * v + 2] = t[4][v];
                        t[4][v] = CONST;
                        t[3][v] = 0;
                    } else {
                        t[3][2 * v + 1] += t[3][v];
                        t[5][2 * v + 1] = 3;
                        t[3][2 * v + 2] += t[3][v];
                        t[5][2 * v + 2] = 3;
                        t[3][v] = 0;
                    }
                } else {
                    t[4][2 * v + 1] = t[4][v];
                    t[3][2 * v + 1] = 0;
                    t[5][2 * v + 1] = 4;
                    t[4][2 * v + 2] = t[4][v];
                    t[3][2 * v + 2] = 0;
                    t[5][2 * v + 2] = 4;
                    t[4][v] = CONST;
                    t[3][v] = 0;
                    t[5][v] = 3;
                }
                return Math.min(rsq(l, r, 2 * v + 1), rsq(l, r, 2 * v + 2));

            }
        }
    }

}