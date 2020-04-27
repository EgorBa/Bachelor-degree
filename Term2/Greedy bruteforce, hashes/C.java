import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class C {

    private static int[] a = new int[7];

    private static int rec(int n) {
        if (n == 0) {
            return 0;
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1000000; i > 0; i /= 10) {
            if (i > n) {
                list.add(a[String.valueOf(i).length() - 1]);
            } else {
                list.add(((n - (n % i)) / i) * a[String.valueOf(i).length() - 1] + rec(n % i));
                list.add((((n - (n % i)) / i) + 1) * a[String.valueOf(i).length() - 1]);
            }
        }
        int ans = Integer.MAX_VALUE;
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            ans = Math.min(ans, list.get(i));
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("printing.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        for (int i = 0; i < 7; i++) {
            a[i] = Integer.parseInt(in.readLine());
        }
        PrintWriter out = new PrintWriter("printing.out");
        out.println(rec(n));
        out.close();
    }
}