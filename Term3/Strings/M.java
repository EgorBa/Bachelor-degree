import java.io.*;
import java.util.*;

public class M {
    private static long m = 923567781L;
    private static HashMap<Long, HashSet<Integer>> map = new HashMap<>();
    private static int p = 29;
    private static long[] h;
    private static long[] h1;
    private static long[] pow;
    private static String str;
    private static String str1;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("common.in"));
        str = in.readLine();
        str1 = in.readLine();
        in.close();
        h = new long[str.length()];
        h1 = new long[str1.length()];
        pow = new long[Math.max(str.length(), str1.length())];
        h[0] = str.charAt(0) - 'a' + 1;
        h1[0] = str1.charAt(0) - 'a' + 1;
        pow[0] = 1;
        for (int i = 1; i < str.length(); i++) {
            h[i] = ((h[i - 1] * p) % m + str.charAt(i) - 'a' + 1) % m;
        }
        for (int i = 1; i < str1.length(); i++) {
            h1[i] = ((h1[i - 1] * p) % m + str1.charAt(i) - 'a' + 1) % m;
        }
        for (int i = 1; i < Math.max(str.length(), str1.length()); i++) {
            pow[i] = (pow[i - 1] * p) % m;
        }
        int l = -1;
        int r = Math.min(str1.length(), str.length()) + 1;
        int ans = -2;
        while (r > l) {
            if (ans == (l + r) / 2) {
                break;
            }
            ans = (r + l) / 2;
            if (check(ans)) {
                l = ans;
            } else {
                r = ans;
            }
        }
        System.out.println(check(3));
        PrintWriter out = new PrintWriter("common.out");
        if (ans == 0) {
            out.println("");
            System.out.println();
        } else {
            ans--;
            map.clear();
            ArrayList<String> list = new ArrayList<>();
            HashSet<Long> set1 = new HashSet<>();
            for (int i = 0; i < str.length() - ans; i++) {
                int a2 = i + ans;
                long hash = (i == 0 ? h[a2] : (h[a2] + 2 * m - ((h[i - 1] * pow[a2 - i + 1]) % m)) % m);
                if (map.containsKey(hash)) {
                    map.get(hash).add(i);
                } else {
                    HashSet<Integer> list1 = new HashSet<>();
                    list1.add(i);
                    map.put(hash, list1);
                }
            }
            for (int i = 0; i < str1.length() - ans; i++) {
                int a2 = i + ans;
                long hash = (i == 0 ? h1[a2] : (h1[a2] + 2 * m - ((h1[i - 1] * pow[a2 - i + 1]) % m)) % m);
                if (map.containsKey(hash)) {
                    for (int j : map.get(hash)) {
                        if (str1.substring(i, a2 + 1).equals(str.substring(j, j + ans + 1))) {
                            list.add(str1.substring(i, a2 + 1));
                        }
                    }
                }
            }
            TreeSet<String> set = new TreeSet<>(list);
            out.println(set.first());
            System.out.println(set.first());
        }
        out.close();
    }

    private static boolean check(int size) {
        size--;
        if (size == -1) {
            return true;
        }
        map.clear();
        for (int i = 0; i < str.length() - size; i++) {
            int a2 = i + size;
            long hash = (i == 0 ? h[a2] : (h[a2] + 2 * m - ((h[i - 1] * pow[a2 - i + 1]) % m)) % m);
            if (map.containsKey(hash)) {
                map.get(hash).add(i);
            } else {
                HashSet<Integer> list = new HashSet<>();
                list.add(i);
                map.put(hash, list);
            }
        }
        for (int i = 0; i < str1.length() - size; i++) {
            int a2 = i + size;
            long hash = (i == 0 ? h1[a2] : (h1[a2] + 2 * m - ((h1[i - 1] * pow[a2 - i + 1]) % m)) % m);
            if (map.containsKey(hash)) {
                for (int j : map.get(hash)) {
                    if (str1.substring(i, a2 + 1).equals(str.substring(j, j + size + 1))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}