import java.io.*;
import java.util.*;

public class F {
    private static long m = 923567781L;
    private static int p = 29;
    private static String[] strs;
    private static boolean flag;
    private static long[][] hash;
    private static long[] pow;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int k = Integer.parseInt(in.readLine());
        strs = new String[k];
        int max_length = Integer.MIN_VALUE;
        int min_length = Integer.MAX_VALUE;
        for (int i = 0; i < k; i++) {
            strs[i] = in.readLine();
            max_length = Math.max(max_length, strs[i].length());
            min_length = Math.min(min_length, strs[i].length());
        }
        Arrays.sort(strs);
        hash = new long[k][max_length];
        if (k == 1) {
            System.out.println(strs[0]);
            System.exit(0);
        }
        pow = new long[max_length];
        pow[0] = 1;
        for (int i = 1; i < pow.length; i++) {
            pow[i] = (pow[i - 1] * p) % m;
        }
        for (int i = 0; i < k; i++) {
            hash[i][0] = strs[i].charAt(0) - 'a' + 1;
            for (int j = 1; j < strs[i].length(); j++) {
                hash[i][j] = ((hash[i][j - 1] * p) % m + strs[i].charAt(j) - 'a' + 1) % m;
            }
        }
        int l = -1;
        int r = min_length + 1;
        int ans = -2;
        while (r > l) {
            if (ans == (l + r) / 2) {
                break;
            }
            ans = (r + l) / 2;
            flag = false;
            if (check(ans, k - 1, new HashMap<>())) {
                l = ans;
            } else {
                r = ans;
            }
        }
        if (ans == 0) {
            System.out.println();
        } else {
            ans--;
            HashMap<Long, HashSet<Integer>> map = new HashMap<>();
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < strs[0].length() - ans; i++) {
                int a2 = i + ans;
                long hash1 = (i == 0 ? hash[0][a2] : (hash[0][a2] + 2 * m - ((hash[0][i - 1] * pow[a2 - i + 1]) % m)) % m);
                if (map.containsKey(hash1)) {
                    map.get(hash1).add(i);
                } else {
                    HashSet<Integer> list1 = new HashSet<>();
                    list1.add(i);
                    map.put(hash1, list1);
                }
            }
            k--;
            while (k != 0) {
                HashMap<Long, HashSet<Integer>> map1 = new HashMap<>();
                for (int i = 0; i < strs[k].length() - ans; i++) {
                    int a2 = i + ans;
                    long hash1 = (i == 0 ? hash[k][a2] : (hash[k][a2] + 2 * m - ((hash[k][i - 1] * pow[a2 - i + 1]) % m)) % m);
                    if (map.containsKey(hash1)) {
                        for (int j : map.get(hash1)) {
                            if (strs[k].substring(i, a2 + 1).equals(strs[0].substring(j, j + ans + 1))) {
                                if (map1.containsKey(hash1)) {
                                    map1.get(hash1).add(j);
                                } else {
                                    HashSet<Integer> list1 = new HashSet<>();
                                    list1.add(j);
                                    map1.put(hash1, list1);
                                }
                            }
                        }
                    }
                }
                map.clear();
                map.putAll(map1);
                k--;
            }
            for (int i = 0; i < strs[0].length() - ans; i++) {
                int a2 = i + ans;
                long hash1 = (i == 0 ? hash[0][a2] : (hash[0][a2] + 2 * m - ((hash[0][i - 1] * pow[a2 - i + 1]) % m)) % m);
                if (map.containsKey(hash1)) {
                    for (int j : map.get(hash1)) {
                        list.add(strs[0].substring(j, j + ans + 1));
                    }
                }
            }
            TreeSet<String> set = new TreeSet<>(list);
            System.out.println(set.first());
        }
    }

    private static boolean check(int size, int k, HashMap<Long, HashSet<Integer>> map) {
        if (k == 0) {
            return !map.isEmpty();
        }
        if (!flag) {
            size--;
            if (size == -1) {
                return true;
            }
            for (int i = 0; i < strs[0].length() - size; i++) {
                int a2 = i + size;
                long hash1 = (i == 0 ? hash[0][a2] : (hash[0][a2] + 2 * m - ((hash[0][i - 1] * pow[a2 - i + 1]) % m)) % m);
                if (map.containsKey(hash1)) {
                    map.get(hash1).add(i);
                } else {
                    HashSet<Integer> list = new HashSet<>();
                    list.add(i);
                    map.put(hash1, list);
                }
            }
            flag = true;
        }
        HashMap<Long, HashSet<Integer>> map1 = new HashMap<>();
        for (int i = 0; i < strs[k].length() - size; i++) {
            int a2 = i + size;
            long hash1 = (i == 0 ? hash[k][a2] : (hash[k][a2] + 2 * m - ((hash[k][i - 1] * pow[a2 - i + 1]) % m)) % m);
            if (map.containsKey(hash1)) {
                for (int j : map.get(hash1)) {
                    if (strs[k].substring(i, a2 + 1).equals(strs[0].substring(j, j + size + 1))) {
                        if (map1.containsKey(hash1)) {
                            map1.get(hash1).add(j);
                        } else {
                            HashSet<Integer> list = new HashSet<>();
                            list.add(j);
                            map1.put(hash1, list);
                        }
                    }
                }
            }
        }
        return check(size, k - 1, map1);
    }
}