import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class B {

    public static class Pair {
        int first, second;

        Pair(int a, int b) {
            first = a;
            second = b;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("request.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        ArrayList<Pair> list = new ArrayList<Pair>(n);
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            list.add(new Pair(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
        int count = 0;
        while (!list.isEmpty()) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).second <= min) {
                    min = list.get(i).second;
                }
            }
            count++;
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).second == min) {
                    list.remove(i);
                }
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).first < min) {
                    list.remove(i);
                }
            }
        }
        in.close();
        PrintWriter out = new PrintWriter("request.out");
        out.println(count);
        out.close();
    }

}
