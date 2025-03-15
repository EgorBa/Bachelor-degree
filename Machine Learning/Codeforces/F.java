import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class F {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int k = Integer.parseInt(in.readLine());
        String[] str = in.readLine().split("\\s");
        int[] l = new int[k];
        for (int i = 0; i < k; i++) {
            l[i] = Integer.parseInt(str[i]);
        }
        int alpha = Integer.parseInt(in.readLine());
        HashSet<String> set = new HashSet<>();
        int n = Integer.parseInt(in.readLine());
        int[] clas = new int[k];
        HashMap<Integer, HashMap<String, Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            str = in.readLine().split("\\s");
            int c = Integer.parseInt(str[0]) - 1;
            clas[c]++;
            map.putIfAbsent(c, new HashMap<>());
            HashMap<String, Integer> cur = map.get(c);
            HashSet<String> curSet = new HashSet<>();
            for (int j = 2; j < str.length; j++) {
                set.add(str[j]);
                if (curSet.add(str[j])) {
                    cur.putIfAbsent(str[j], 0);
                    cur.put(str[j], cur.get(str[j]) + 1);
                }
            }
        }
        HashMap<Integer, HashMap<String, Double>> prob = new HashMap<>();
        for (int i = 0; i < k; i++) {
            prob.putIfAbsent(i, new HashMap<>());
            HashMap<String, Double> cur = prob.get(i);
            for (String j : set) {
                cur.put(j, ((double) (map.getOrDefault(i, new HashMap<>()).getOrDefault(j, 0) + alpha) / (clas[i] + 2 * alpha)));
            }
        }
        if (alpha == 1 && n < 100) {
            int m = Integer.parseInt(in.readLine());
            for (int i = 0; i < m; i++) {
                str = in.readLine().split("\\s");
                HashSet<String> set1 = new HashSet<>(Arrays.asList(str).subList(1, str.length));
                double[] c1 = new double[k];
                double count = 0;
                for (int j = 0; j < k; j++) {
                    c1[j] = 1;
                    for (String st : set) {
                        if (set1.contains(st)) {
                            c1[j] *= prob.get(j).get(st);
                        } else {
                            c1[j] *= (1 - prob.get(j).get(st));
                        }
                    }
                    c1[j] *= (double) clas[j] / n;
                    c1[j] *= l[j];
                    count += c1[j];
                }
                for (int j = 0; j < k; j++) {
                    try {
                        System.out.print((c1[j] / count) + " ");
                    } catch (Exception e) {
                        System.out.print(0 + " ");
                    }
                }
                System.out.println();
            }
        } else {
            int m = Integer.parseInt(in.readLine());
            for (int i = 0; i < m; i++) {
                str = in.readLine().split("\\s");
                HashSet<String> set1 = new HashSet<>(Arrays.asList(str).subList(1, str.length));
                double[] c1 = new double[k];
                for (int j = 0; j < k; j++) {
                    for (String st : set) {
                        if (set1.contains(st)) {
                            c1[j] += Math.log(prob.get(j).get(st));
                        } else {
                            c1[j] += Math.log(1 - prob.get(j).get(st));
                        }
                    }
                    c1[j] += Math.log(((double) clas[j]) / ((double) n));
                    c1[j] += Math.log(l[j]);
                }
                for (int j = 0; j < k; j++) {
                    double count = 0;
                    for (int t = 0; t < k; t++) {
                        count += Math.exp(c1[t] - c1[j]);
                    }
                    System.out.print(java.math.BigDecimal.valueOf(1 / count).toString() + " ");
                }
                System.out.println();
            }
        }
    }
}