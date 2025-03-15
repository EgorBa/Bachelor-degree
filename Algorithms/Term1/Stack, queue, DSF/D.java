import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class D {

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
        String[] s;
        int n = scan.nextInt();
        List<Integer> first = new LinkedList<>();
        List<Integer> second = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            s = scan.nextLine().split("[\\s]");
            if (s[0].equals("+")) {
                ((LinkedList<Integer>) second).addLast(Integer.parseInt(s[1]));
            } else {
                if (s[0].equals("*")) {
                    ((LinkedList<Integer>) second).addFirst(Integer.parseInt(s[1]));
                } else {
                    System.out.print(((LinkedList<Integer>) first).getFirst() + "\n");
                    ((LinkedList<Integer>) first).removeFirst();
                }
            }
            if (first.size() < second.size()) {
                ((LinkedList<Integer>) first).addLast(((LinkedList<Integer>) second).getFirst());
                ((LinkedList<Integer>) second).removeFirst();
            }
        }
    }
}