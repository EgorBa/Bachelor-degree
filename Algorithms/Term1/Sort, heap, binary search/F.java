import java.util.Scanner;

public class F {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int k = scan.nextInt();
        int[] a = new int[n];
        int[] b = new int[k];
        for (int i = 0; i < n; i++) {
            a[i] = scan.nextInt();
        }
        for (int i = 0; i < k; i++) {
            b[i] = scan.nextInt();
            if (b[i] <= a[0]) {
                System.out.println(a[0]);
                continue;
            }
            if (b[i] >= a[n - 1]) {
                System.out.println(a[n - 1]);
                continue;
            }
            int r = 0;
            int l = n;
            int j = (r + l) / 2;
            while (b[i] != a[j] && r < l) {
                if (b[i] > a[j]) {
                    r = j;
                }
                if (b[i] < a[j]) {
                    l = j;
                }
                j = (r + l) / 2;
                if (b[i] >= a[j] && b[i] < a[j + 1]) {
                    break;
                }
            }
            if (a[j + 1] - b[i] < b[i] - a[j]) {
                System.out.println(a[j + 1]);
            } else {
                System.out.println(a[j]);
            }
        }

    }
}