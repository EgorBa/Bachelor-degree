import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class H {
    public static ArrayList<String> list = new ArrayList<>();

    private static void print(String str, int n, int k) {
        if (n == 0) {
            list.add(str);
            System.out.println(str);
            return;
        }
        if (k == 1) {
            n = n - 1;
            k = 0;
            print(str + "0", n, k);
        } else {
            n = n - 1;
            print(str + "0", n, k);
            print(str + "1", n, k + 1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("vectors2.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        print("", n, 0);
        in.close();
        PrintWriter out = new PrintWriter("vectors2.out");
        out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.println(list.get(i));
        }
        out.close();
    }

}
