import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

public class A {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] strs = in.readLine().split("[\\s]");
        int n = Integer.parseInt(strs[0]);
        int m = Integer.parseInt(strs[1]);
        int k = Integer.parseInt(strs[2]);
        int[] a = new int[n];
        HashSet<Integer>[] pos = new HashSet[m];
        HashSet<Integer>[] ans = new HashSet[k];
        strs = in.readLine().split("[\\s]");
        for (int i = 0; i < m; i++) {
            pos[i] = new HashSet<>();
        }
        for (int i = 0; i < k; i++) {
            ans[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(strs[i]);
            pos[a[i] - 1].add(i + 1);
        }
        int count = 0;
        for (int i = 0; i < m; i++) {
            if (pos[i].size() > 0) {
                for (int cur : pos[i]) {
                    ans[count].add(cur);
                    count = (count + 1) % k;
                }
            }
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < k; i++) {
            out.write(ans[i].size() + " ");
            for (int j : ans[i]) {
                out.write(j + " ");
            }
            out.newLine();
        }
        out.close();
    }
}