import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class A {
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
        int[] min = new int[n];
        int j = -1;
        for (int i = 0; i < n; i++) {
            int a = scan.nextInt();
            if (a == 1) {
                int b = scan.nextInt();
                j++;
                if (j > 0) {
                    if (b > min[j - 1]) {
                        min[j] = min[j - 1];
                    } else {
                        min[j] = b;
                    }
                } else {
                    min[j] = b;
                }
            } else {
                if (a == 2) {
                    j--;
                } else {
                    System.out.print(min[j] + "\n");
                }
            }
        }
    }
}