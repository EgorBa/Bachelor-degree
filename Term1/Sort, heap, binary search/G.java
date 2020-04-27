import java.util.Scanner;

public class G {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int x = scan.nextInt();
        int y = scan.nextInt();
        int t = 0;
        n--;
        if (n != 0) {
            int r = t;
            int l = Integer.MAX_VALUE;
            t = (r + l) / 2;
            while (t / x + t / y != n && r < l) {
                if (t / x + t / y < n) {
                    r = t;
                }
                if (t / x + t / y > n) {
                    l = t;
                }
                t = (r + l) / 2;
                if (n > (t - 1) / x + (t - 1) / y && n <= (t) / x + (t) / y) {
                    break;
                }

            }
            if (t / x + t / y == n) {
                while (t / x + t / y == n) {
                    t--;
                }
                t++;
            }
        }
        if (x > y) {
            t = t + y;
        } else {
            t = t + x;
        }
        System.out.println(t);
    }

}