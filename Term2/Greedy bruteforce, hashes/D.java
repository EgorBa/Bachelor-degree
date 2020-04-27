import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class D {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("sequence.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int[] a = new int[n];
        split = in.readLine().split("[\\s]");
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(split[i]);
            sum += a[i];
        }
        in.close();
        ArrayList<Integer> list = new ArrayList<>();
        boolean flag = false;
        if (sum % 2 == 1) {
            flag = false;
        } else {
            sum = sum / 2;
            for (int i = n - 1; i >= 0; i--) {
                if (a[i] <= sum) {
                    sum -= a[i];
                    list.add(i + 1);
                }
                if (sum == 0) {
                    flag = true;
                    break;
                }
            }
        }
        PrintWriter out = new PrintWriter("sequence.out");
        if (!flag) {
            out.println(-1);
        } else {
            out.println(list.size());
            for (int i : list) {
                out.print(i + " ");
            }
        }
        out.close();
    }
}