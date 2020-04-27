import org.omg.PortableInterceptor.INACTIVE;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class A {

    public static void main(String s[]) throws IOException {
        BufferedReader scan = new BufferedReader(new FileReader("input.txt"));
        String[] split = scan.readLine().split("[\\s]");
        int n = (int) Double.parseDouble(split[0]);
        int k = (int) Double.parseDouble(split[1]);
        File out = new File("output.txt");
        PrintWriter writer = new PrintWriter(out, "UTF8");
        int[] a = new int[n - 2];
        split = scan.readLine().split("[\\s]");
        for (int i = 0; i < n - 2; i++) {
            a[i] = Integer.parseInt(split[i]);
        }
        int[] dp = new int[n];
        dp[0] = 0;
        int u = n - 1;
        List<Integer> list = new ArrayList();
        list.add(1);
        for (int i = 1; i < n; i++) {
            int max = Integer.MIN_VALUE;
            int y = 0;
            int j = i;
            while (j > 0 && y != k) {
                y++;
                j--;
                max = Math.max(dp[j], max);
            }
            if (i != n - 1) {
                dp[i] = a[i - 1] + max;
            } else {
                dp[i] = max;
            }
        }
        writer.println(dp[n - 1]);
        while (u != 0) {
            int j = u;
            int y = 0;
            while (j > 0 && y != k) {
                y++;
                j--;
                if (u != n - 1) {
                    if (dp[u] - a[u - 1] == dp[j]) {
                        list.add(u + 1);
                        u = j;
                        break;
                    }
                } else {
                    if (dp[u] == dp[j]) {
                        list.add(u + 1);
                        u = j;
                        break;
                    }
                }
            }
        }
        Collections.sort(list);
        writer.println(list.size() - 1);
        for (int i = 0; i < list.size(); i++) {
            writer.print(list.get(i) + " ");
        }
        scan.close();
        writer.close();
    }
}