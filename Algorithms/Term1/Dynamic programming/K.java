import java.io.*;

public class K {
    public static BufferedReader scan;

    static {
        try {
            scan = new BufferedReader(new FileReader("skyscraper.in"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static File out = new File("skyscraper.out");
    static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(out, "UTF8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static void answer(int n, boolean bool, int[] a, int w, int[] b) {
        if (n == 0) {
            return;
        }
        int[] sum = new int[1 << n];
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    sum[i] += a[j];
                }
            }
        }

        int[] ints = new int[1 << n];
        int all = (1 << n) - 1;
        int c = 1;
        while (true) {
            if (c > 1) {
                for (int i = 0; i <= (1 << n) - 1; i++) {
                    for (int j = 0; j < n; j++) {
                        if ((i & (1 << j)) == 0) {
                            ints[i | (1 << j)] = Math.max(ints[i | (1 << j)], ints[i]);
                        }
                    }
                }
            }

            if (sum[all] - ints[all] <= w) {
                if (bool) {
                    writer.println(c);
                }
                for (int p = 0; p < (1 << n); p++) {
                    if (sum[p] == ints[p] && sum[all] - ints[p] <= w) {
                        int p1 = p;
                        int c1 = all & (~p1);
                        writer.print(Integer.bitCount(c1));
                        for (int i = n - 1; i >= 0; i--) {
                            if ((c1 & (1 << i)) != 0) {
                                writer.print(" " + b[i]);
                            }
                        }
                        writer.println();
                        int nF = 0;
                        for (int i = 0; i < n; i++) {
                            if ((p1 & (1 << i)) != 0) {
                                a[nF] = a[i];
                                b[nF] = b[i];
                                nF++;
                            }
                        }
                        answer(nF, false, a, w, b);
                        return;
                    }
                }
            }
            for (int i = 0; i < (1 << n); i++) {
                ints[i] = sum[i] - ints[i] <= w ? sum[i] : 0;
            }
            c++;
        }
    }

    public static boolean check(int[] dp) {
        boolean ans = false;
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] != 0) {
                ans = true;
                break;
            }
        }
        return ans;
    }

    public static void main(String args[]) throws IOException {
        String[] split = scan.readLine().split("[\\s]");
        int n = (int) Double.parseDouble(split[0]);
        int w = (int) Double.parseDouble(split[1]);
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(scan.readLine());
            b[i] = i + 1;
        }
        answer(n, true, a, w, b);
        writer.close();
    }
}