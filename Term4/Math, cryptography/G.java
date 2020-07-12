import java.io.*;
import java.math.BigInteger;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

public class G {

    private static class Complex {
        private double mRe;
        private double mIm;

        public Complex(double re, double im) {
            mRe = re;
            mIm = im;
        }

        public double getRe() {
            return mRe;
        }

        public double getIm() {
            return mIm;
        }

        public Complex multi(Complex a) {
            double re = mRe * a.mRe - mIm * a.mIm;
            double im = mRe * a.mIm + mIm * a.mRe;
            return new Complex(re, im);
        }

        public Complex add(Complex a) {
            double re = mRe + a.mRe;
            double im = a.mIm + mIm;
            return new Complex(re, im);
        }

        public Complex minus(Complex a) {
            double re = mRe - a.mRe;
            double im = mIm - a.mIm;
            return new Complex(re, im);
        }

        public Complex multi(int a) {
            double re = mRe * a;
            return new Complex(re, mIm);
        }

        public Complex del(double a) {
            double re = mRe / a;
            double im = mIm / a;
            return new Complex(re, im);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("duel.in"));
        String a = in.readLine();
        in.close();
        in.close();
        int size = 1;
        while (size < a.length()) {
            size *= 2;
        }
        ArrayList<Complex> x = new ArrayList<>();
        ArrayList<Complex> y = new ArrayList<>();
        ArrayList<Integer> xy = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < a.length(); i++) {
            char chr = a.charAt(i);
            if (chr == '-') continue;
            x.add(new Complex(chr - 48, 0));
            j++;
        }
        while (j != 2 * size) {
            x.add(new Complex(0, 0));
            j++;
        }
        j = 0;
        for (int i = 0; i < a.length(); i++) {
            char chr = a.charAt(i);
            if (chr == '-') continue;
            y.add(new Complex(chr - 48, 0));
            j++;
        }
        while (j != 2 * size) {
            y.add(new Complex(0, 0));
            j++;
        }
        fft(x, false);
        fft(y, false);
        for (int i = 0; i < 2 * size; i++) {
            x.set(i, x.get(i).multi(y.get(i)));
        }
        fft(x, true);
        for (int i = 0; i < 2 * size; i++) {
            xy.add((int) (x.get(i).mRe + 0.5));
        }
        long ans = 0;
        for (int i : xy) {
            if (i > 1 && i % 2 == 1) {
                ans += (i / 2);
            }
        }
        PrintWriter out = new PrintWriter("duel.out");
        out.println(ans);
        out.close();
    }

    private static void fft(ArrayList<Complex> a, boolean invert) {
        int n = a.size();
        if (n == 1) return;
        ArrayList<Complex> a0 = new ArrayList<>();
        ArrayList<Complex> a1 = new ArrayList<>();
        for (int i = 0; i < n / 2; i++) {
            a0.add(new Complex(0, 0));
            a1.add(new Complex(0, 0));
        }
        for (int i = 0, j = 0; i < n; i += 2, j++) {
            a0.set(j, a.get(i));
            a1.set(j, a.get(i + 1));
        }
        fft(a0, invert);
        fft(a1, invert);
        double ang = 2 * PI / n * (invert ? -1 : 1);
        Complex w = new Complex(1, 0);
        Complex wn = new Complex(cos(ang), sin(ang));
        for (int i = 0; i < n / 2; ++i) {
            a.set(i, a0.get(i).add(w.multi(a1.get(i))));
            a.set(i + n / 2, a0.get(i).minus(w.multi(a1.get(i))));
            if (invert) {
                a.set(i, a.get(i).del(2));
                a.set(i + n / 2, a.get(i + n / 2).del(2));
            }
            w = w.multi(wn);
        }
    }
}