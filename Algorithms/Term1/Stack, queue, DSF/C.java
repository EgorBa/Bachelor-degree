import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class C {
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

    public static void main(String[] args) throws IOException {
        FastReader scan = new FastReader(System.in);
        String[] s = scan.nextLine().split("[\\s]");
        int n = Integer.parseInt(s[0]);
        int[] o = new int[n];
        int beg = 0, end = -1, b = 0;
        for (int i = 0; i < n; i++) {
            int a = scan.nextInt();
            if (a == 1) {
                b = scan.nextInt();
                end++;
                o[end] = b;
            }
            if (a == 2) {
                beg++;
            }
            if (a == 3) {
                end--;
            }
            if (a == 4) {
                b = scan.nextInt();
                int j = beg;
                while (b != o[j]) {
                    j++;
                }
                System.out.print(j - beg + "\n");
            }
            if (a == 5) {
                System.out.print(o[beg] + "\n");
            }
        }
    }
}