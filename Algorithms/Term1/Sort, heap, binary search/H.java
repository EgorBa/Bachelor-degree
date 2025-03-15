import java.util.Scanner;
import java.math.BigInteger;

public class H {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        BigInteger w = scan.nextBigInteger();
        BigInteger h = scan.nextBigInteger();
        BigInteger n = scan.nextBigInteger();
        BigInteger r = new BigInteger("0");
        BigInteger l = new BigInteger("1000000000000000000000000000");
        BigInteger t = r.add(l).divide(new BigInteger("2"));

        while (t.divide(w).multiply(t.divide(h)).compareTo(n) != 0 && r.compareTo(l) < 0) {
            if (t.divide(w).multiply(t.divide(h)).compareTo(n) < 0) {
                r = t;
            }
            if (t.divide(w).multiply(t.divide(h)).compareTo(n) > 0) {
                l = t;
            }
            t = r.add(l).divide(new BigInteger("2"));
            if (t.subtract(new BigInteger("1")).divide(w).multiply(t.subtract(new BigInteger("1")).divide(h)).compareTo(n) < 0 &&
                    t.divide(w).multiply(t.divide(h)).compareTo(n) >= 0) {
                break;
            }
        }
        if (t.divide(w).multiply(t.divide(h)).compareTo(n) == 0) {
            if (t.mod(w).compareTo(t.mod(h)) > 0) {
                t = t.subtract(t.mod(h));
            } else {
                t = t.subtract(t.mod(w));
            }
        }
        System.out.println(t);
    }
}