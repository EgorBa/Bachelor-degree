import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class J {
    public static int x;
    public static int[][] t;
    public static int answer;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] split = reader.readLine().split("[\\s]");
        int n = Integer.valueOf(split[0]);
        int m = Integer.valueOf(split[1]);
        x = 1;
        while (x <= n) {
            x *= 2;
        }
        t = new int[4][2 * x - 1];
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
            if (split[0].equals("defend")) {
                set(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]), 0);
            } else {
                answer = 0;
                int u = rsq(Integer.valueOf(split[1]), Integer.valueOf(split[2]), 0);
                System.out.print(u + " ");
                answer = find(Integer.valueOf(split[1]), Integer.valueOf(split[2]), 0, u);
                while (answer < x - 1) {
                    if (t[3][answer] == 1) {
                        if (t[0][2 * answer + 1] < t[0][answer]) {
                            t[3][2 * answer + 1] = 1;
                            t[0][2 * answer + 1] = t[0][answer];
                        }
                        if (t[0][2 * answer + 2] < t[0][answer]) {
                            t[3][2 * answer + 2] = 1;
                            t[0][2 * answer + 2] = t[0][answer];
                        }
                        t[3][answer] = 0;
                    }
                    t[0][answer] = Math.min(t[0][2 * answer + 1], t[0][2 * answer + 2]);
                    if (t[0][2 * answer + 1] == t[0][answer]) {
                        answer = 2 * answer + 1;
                    } else {
                        answer = 2 * answer + 2;
                    }
                }
                System.out.println(answer - (x - 2));
            }
        }
    }

    private static void set(int l, int r, int x, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                if (t[1][v] == t[2][v]) {
                    t[0][v] = x;
                } else {
                    if ((t[0][2 * v + 1] >= x) && (t[0][2 * v + 2] >= x)) {
                        t[0][v] = Math.min(t[0][2 * v + 1], t[0][2 * v + 2]);
                    } else {
                        t[0][v] = x;
                        t[3][v] = 1;
                    }
                }
            } else {
                if (t[3][v] == 1) {
                    if (t[0][2 * v + 1] < t[0][v]) {
                        t[3][2 * v + 1] = 1;
                        t[0][2 * v + 1] = t[0][v];
                    }
                    if (t[0][2 * v + 2] < t[0][v]) {
                        t[3][2 * v + 2] = 1;
                        t[0][2 * v + 2] = t[0][v];
                    }
                    t[3][v] = 0;
                }
                if (t[0][2 * v + 1] < x) {
                    set(l, r, x, 2 * v + 1);
                }
                if (t[0][2 * v + 2] < x) {
                    set(l, r, x, 2 * v + 2);
                }
                t[0][v] = Math.min(t[0][2 * v + 1], t[0][2 * v + 2]);
            }
        }
    }


    private static int rsq(int l, int r, int v) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
            return Integer.MAX_VALUE;
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                return t[0][v];
            } else {
                if (t[3][v] == 1) {
                    if (t[0][2 * v + 1] < t[0][v]) {
                        t[3][2 * v + 1] = 1;
                        t[0][2 * v + 1] = t[0][v];
                    }
                    if (t[0][2 * v + 2] < t[0][v]) {
                        t[3][2 * v + 2] = 1;
                        t[0][2 * v + 2] = t[0][v];
                    }
                    t[3][v] = 0;
                }
                t[0][v] = Math.min(t[0][2 * v + 1], t[0][2 * v + 2]);
                return Math.min(rsq(l, r, 2 * v + 1), rsq(l, r, 2 * v + 2));
            }
        }
    }

    private static int find(int l, int r, int v, int x) {
        if ((t[2][v] < l) || (t[1][v] > r)) {
            return Integer.MAX_VALUE;
        } else {
            if ((t[1][v] >= l) && (t[2][v] <= r)) {
                if (t[0][v] == x) {
                    return v;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else {
                if (t[3][v] == 1) {
                    if (t[0][2 * v + 1] < t[0][v]) {
                        t[3][2 * v + 1] = 1;
                        t[0][2 * v + 1] = t[0][v];
                    }
                    if (t[0][2 * v + 2] < t[0][v]) {
                        t[3][2 * v + 2] = 1;
                        t[0][2 * v + 2] = t[0][v];
                    }
                    t[3][v] = 0;
                }
                t[0][v] = Math.min(t[0][2 * v + 1], t[0][2 * v + 2]);
                return Math.min(find(l, r, 2 * v + 1, x), find(l, r, 2 * v + 2, x));
            }
        }
    }
}