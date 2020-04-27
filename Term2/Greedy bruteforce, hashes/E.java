import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

public class E {

    private static class Pair {
        int first;
        int second;
        int third;

        Pair(int a, int b, int p) {
            first = a;
            second = b;
            third = p;
        }
    }

    private static boolean comp(Pair a, Pair b) {
        int first = a.second - a.first;
        int second = b.second - b.first;
        if (first < 0) {
            if (second < 0) {
                return a.second > b.second;
            }
            return false;
        }
        if (second < 0) {
            return true;
        }
        return a.first < b.first;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("apples.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int s = Integer.parseInt(split[1]);
        Pair[] b = new Pair[n];
        int[][] a = new int[n][4];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            a[i][0] = Integer.parseInt(split[0]);
            a[i][1] = Integer.parseInt(split[1]);
            b[i] = new Pair(a[i][0], a[i][1], i + 1);
            a[i][2] = -a[i][0] + a[i][1];
        }
        in.close();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (comp(b[i], b[j])) {
                    Pair tmp = b[i];
                    b[i] = b[j];
                    b[j] = tmp;
                }
            }
        }
        boolean flag = false;
        for (int i = n - 1; i >= 0; i--) {
            s -= b[i].first;
            if (s <= 0) {
                flag = true;
                break;
            }
            s += b[i].second;
        }
        PrintWriter out = new PrintWriter("apples.out");
        if (flag) {
            out.println(-1);
        } else {
            for (int i = n - 1; i >= 0; i--) {
                out.print(b[i].third + " ");
            }
        }
        out.close();
    }
}