import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class G {

    public static int find(int[] a, int s) {
        if (a[s] == s) {
            return s;
        } else {
            return a[s] = find(a, a[s]);
        }
    }

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

    public static void main(String[] args) {
        FastReader scan = new FastReader(System.in);
        int n = Integer.parseInt(scan.nextLine());
        int[] max = new int[n + 1];
        int[] min = new int[n + 1];
        int[] count = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            max[i] = i;
            min[i] = i;
            count[i] = 1;
        }
        try {
            while (true) {
                String[] s = scan.nextLine().split("[\\s]");
                if (s[0].equals("get")) {
                    int mi = find(min, Integer.parseInt(s[1]));
                    int ma = find(max, Integer.parseInt(s[1]));
                    System.out.println(mi + " " + ma + " " + count[ma]);
                }
                if (s[0].equals("union")) {
                    int a1 = Integer.parseInt(s[1]);
                    int a2 = Integer.parseInt(s[2]);
                    int l1 = find(max, a1);
                    int l2 = find(max, a2);
                    int l3 = find(min, a1);
                    int l4 = find(min, a2);
                    int a3 = Math.max(l1, l2);
                    int a5 = Math.min(l1, l2);
                    int a4 = Math.min(l3, l4);
                    int a6 = Math.max(l3, l4);
                    if (a3 == a5) {
                    } else {
                        count[a3] = count[a3] + count[a5];
                    }
                    max[a5] = a3;
                    max[a1] = a3;
                    max[a2] = a3;
                    min[a6] = a4;
                    min[a1] = a4;
                    min[a2] = a4;
                }
                if (s[0].equals(null)) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
        }
    }
}