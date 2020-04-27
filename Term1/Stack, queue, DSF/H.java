import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class H {

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader(InputStream in) {
            br = new BufferedReader(new
                    InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public static int get(int k, int[][] a, int sum) {
        if (k == a[0][k]) {
            return sum + a[1][k];
        } else {
            return get(a[0][k], a, sum + a[1][k]);
        }
    }

    public static int find(int k, int[][] a) {
        if (k == a[0][k]) {
            return a[0][k];
        } else {
            return find(a[0][k], a);
        }
    }

    public static void add(int k, int sum, int[][] a) {
        int y = find(k, a);
        a[1][y] = a[1][y] + sum;
    }

    public static void join(int k1, int k2, int[][] a, int[] r) {
        k1 = find(k1, a);
        k2 = find(k2, a);
        if (k1 != k2) {
            if (r[k1] == r[k2]) {
                r[k1]++;
            }
            if (r[k1] < r[k2]) {
                int y = k1;
                k1 = k2;
                k2 = y;
            }
            a[0][k2] = a[0][k1];
            a[1][k2] = a[1][k2] - a[1][k1];
        }
    }

    public static void main(String[] args) {
        FastReader scan = new FastReader(System.in);
        String[] split = scan.nextLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int k = Integer.parseInt(split[1]);
        int[][] a = new int[2][n + 1];
        int[] r = new int[n + 1];
        for (int y = 1; y <= n; y++) {
            a[0][y] = y;
        }
        for (int i = 0; i < k; i++) {
            split = scan.nextLine().split("[\\s]");
            if (split[0].equals("get")) {
                System.out.println(get(Integer.parseInt(split[1]), a, 0));
            } else {
                if (split[0].equals("join")) {
                    join(Integer.parseInt(split[1]), Integer.parseInt(split[2]), a, r);
                } else {
                    add(Integer.parseInt(split[1]), Integer.parseInt(split[2]), a);
                }
            }
        }
    }
}