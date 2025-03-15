import java.util.Arrays;
import java.io.*;

public class E {
    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(scan.readLine());
        int[] a = new int[n];
        String[] s3 = scan.readLine().split("[\\s]");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(s3[i]);
        }
        int j, j1;
        Arrays.sort(a);
        int k = Integer.parseInt(scan.readLine());
        for (int i = 0; i < k; i++) {
            String[] s2 = scan.readLine().split("[\\s]");
            int s = Integer.parseInt(s2[0]);
            int s1 = Integer.parseInt(s2[1]);
            if (s > a[n - 1]) {
                System.out.println(0);
            } else {
                if (s <= a[0]) {
                    j = 0;
                } else {
                    int r = 0;
                    int l = n;
                    j = (r + l) / 2;
                    while (!(s <= a[j] && a[j - 1] < s) && r < l) {
                        if (s > a[j]) {
                            r = j;
                        }
                        if (s <= a[j]) {
                            l = j;
                        }
                        j = (r + l) / 2;
                    }
                }
                if (a[0] > s1) {
                    System.out.println(0);
                } else {
                    if (a[n - 1] <= s1) {
                        j1 = n - 1;
                    } else {
                        int r = 0;
                        int l = n;
                        j1 = (r + l) / 2;
                        while (!(s1 >= a[j1] && s1 < a[j1 + 1]) && r < l) {
                            if (s1 >= a[j1]) {
                                r = j1;
                            }
                            if (s1 < a[j1]) {
                                l = j1;
                            }
                            j1 = (r + l) / 2;
                        }
                    }
                    if (a[j] >= s && a[j1] <= s1) {
                        System.out.println(j1 - j + 1);
                    } else {
                        if (a[j] >= s || a[j1] <= s1) {
                            System.out.println(j1 - j);
                        } else {
                            System.out.println(j1 - j - 1);
                        }

                    }
                }
            }
        }
    }
}