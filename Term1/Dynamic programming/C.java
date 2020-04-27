import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class C {

    public static void main(String s[]) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        String[] split = scan.readLine().split("[\\s]");
        int n = (int) Double.parseDouble(split[0]);
        int[] a = new int[n];
        split = scan.readLine().split("[\\s]");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(split[i]);
        }
        List<Integer> list = new ArrayList();
        int[] dp = new int[n];
        dp[0] = 1;
        for (int i = 0; i < n; i++) {
            int j = i;
            int max = 0;
            for (j = i; j >= 0; j--) {
                if (a[j] < a[i]) {
                    max = Math.max(max, dp[j]);
                }
            }
            dp[i] = max + 1;
        }
        int max = 0;
        int j = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(dp[i], max);
            if (dp[i] == max) {
                j = i;
            }
        }
        System.out.println(max);
        list.add(a[j]);
        while (j != -1) {
            if (dp[j] == max - 1) {
                list.add(a[j]);
                max--;
            }
            j--;
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
    }
}