import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;

public class A {
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("cobbler.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int k = Integer.parseInt(split[1]);
        split = in.readLine().split("[\\s]");
        int[] a = new int[k];
        for (int i = 0; i < k; i++) {
            a[i] = Integer.parseInt(split[i]);
        }
        Arrays.sort(a);
        int i = 0;
        int count = -1;
        while (n >= 0) {
            try {
                n -= a[i];
            } catch (Exception e) {
                count++;
                break;
            }
            i++;
            count++;
        }
        in.close();
        PrintWriter out = new PrintWriter("cobbler.out");
        out.println(count);
        out.close();
    }
}
