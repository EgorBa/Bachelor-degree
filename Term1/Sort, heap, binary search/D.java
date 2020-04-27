import java.util.Scanner;

public class D {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] a = new int[2 * n];
        int x = 0;
        for (int i = 0; i < n; i++) {
            int l = scan.nextInt();
            if (l == 0) {
                l = scan.nextInt();
                a[x] = l;
                int x1 = x;
                while (a[x1] > a[(x1 - 1) / 2]) {
                    int t = a[x1];
                    a[x1] = a[(x1 - 1) / 2];
                    a[(x1 - 1) / 2] = t;
                    x1 = (x1 - 1) / 2;
                }
                x++;
            } else {
                System.out.println(a[0]);
                a[0] = a[x];
                a[x] = 0;
                int x1 = 0;
                while (a[x1] < a[x1 * 2 + 1] || a[x1] < a[x1 * 2 + 2]) {
                    if (a[x1] < a[x1 * 2 + 1] && a[x1 * 2 + 1] > a[x1 * 2 + 2]) {
                        int t = a[x1];
                        a[x1] = a[x1 * 2 + 1];
                        a[x1 * 2 + 1] = t;
                        x1 = x1 * 2 + 1;
                    } else {
                        int t = a[x1];
                        a[x1] = a[x1 * 2 + 2];
                        a[x1 * 2 + 2] = t;
                        x1 = x1 * 2 + 2;
                    }
                }
            }
        }
    }
}