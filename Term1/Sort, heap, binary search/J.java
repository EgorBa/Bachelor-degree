import java.util.Scanner;

public class J {
    public static double eps = 0.000001;

    public static double f(double a, double e, int c, int b) {
        return Math.sqrt(a * a + (e - 1) * (e - 1)) / c + Math.sqrt((a - 1) * (a - 1) + e * e) / b;
    }

    public static double ternarySearchMin(double left, double right, double e1, int c1, int b1) {
        if (right - left < eps) {
            return (left + right) / 2;
        }
        double a = (left * 2 + right) / 3;
        double b = (left + right * 2) / 3;
        if (f(a, e1, c1, b1) < f(b, e1, c1, b1)) {
            return ternarySearchMin(left, b, e1, c1, b1);
        } else {
            return ternarySearchMin(a, right, e1, c1, b1);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int c = scan.nextInt();
        int b = scan.nextInt();
        double e = scan.nextDouble();
        System.out.println(ternarySearchMin(-1, 1000000, e, c, b));
    }
}