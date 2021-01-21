import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class C {
    public static String funcDist;
    public static String funcK;
    public static String funcW;
    public static Point x;

    public static class Point {
        int[] signs;
        int value;

        Point(int[] signs, int value) {
            this.signs = signs;
            this.value = value;
        }

        Point(int[] signs) {
            this.signs = signs;
            this.value = 0;
        }

        double dist(Point p) {
            double ans = 0;
            switch (funcDist) {
                case ("manhattan"): {
                    for (int i = 0; i < signs.length; i++) {
                        ans += Math.abs(signs[i] - p.signs[i]);
                    }
                    return ans;
                }
                case ("euclidean"): {
                    for (int i = 0; i < signs.length; i++) {
                        ans += Math.abs(signs[i] - p.signs[i]) * Math.abs(signs[i] - p.signs[i]);
                    }
                    ans = Math.sqrt(ans);
                    return ans;
                }
                case ("chebyshev"): {
                    for (int i = 0; i < signs.length; i++) {
                        ans = Math.max(ans, Math.abs(signs[i] - p.signs[i]));
                    }
                    return ans;
                }
                default:
                    return 0;
            }
        }
    }

    public static class Sort implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return Double.compare(o1.dist(x), o2.dist(x));
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] strs = in.readLine().split("[\\s]");
        int n = Integer.parseInt(strs[0]);
        int m = Integer.parseInt(strs[1]);
        Point[] points = new Point[n];
        for (int i = 0; i < n + 1; i++) {
            strs = in.readLine().split("[\\s]");
            int[] a = new int[m];
            for (int j = 0; j < m; j++) {
                a[j] = Integer.parseInt(strs[j]);
            }
            if (strs.length == m + 1) {
                points[i] = new Point(a, Integer.parseInt(strs[m]));
            } else {
                x = new Point(a);
            }
        }
        funcDist = in.readLine();
        funcK = in.readLine();
        funcW = in.readLine();
        int h = Integer.parseInt(in.readLine());
        double k;
        if (funcW.equals("variable")) {
            Arrays.sort(points, new Sort());
            k = x.dist(points[h]);
        } else {
            k = h;
        }
        double first = 0;
        double second = 0;
        double ans = 0;
        if (k == 0) {
            double sum = 0;
            double count = 0;
            for (int i = 0; i < n; i++) {
                if (x.dist(points[i]) == 0) {
                    sum += points[i].value;
                    count++;
                }
            }
            if (count == 0) {
                for (int i = 0; i < n; i++) {
                    sum += points[i].value;
                    count++;
                }
            }
            ans = sum / count;
        } else {
            for (int i = 0; i < n; i++) {
                double v = x.dist(points[i]) / k;
                first += points[i].value * funcK(v);
                second += funcK(v);
            }
            if (second != 0) {
                ans = first / second;
            } else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < n; i++) {
                    sum += points[i].value;
                    count++;
                }
                ans = sum / count;
            }
        }
        PrintWriter out = new PrintWriter(System.out);
        out.println(ans);
        out.close();
    }

    public static double funcK(double u) {
        switch (funcK) {
            case ("uniform"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                return 0.5;
            }
            case ("triangular"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                return (1 - Math.abs(u));
            }
            case ("epanechnikov"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                return (0.75) * (1 - u * u);
            }
            case ("quartic"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                return (15d / 16d) * (1 - u * u) * (1 - u * u);
            }
            case ("triweight"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                return (35d / 32d) * (1 - u * u) * (1 - u * u) * (1 - u * u);
            }
            case ("tricube"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                double v = 1 - u * u * Math.abs(u);
                return (70d / 81d) * v * v * v;
            }
            case ("gaussian"): {
                double v = Math.exp(-0.5 * u * u);
                return v / Math.sqrt(2 * Math.PI);
            }
            case ("cosine"): {
                if (Math.abs(u) >= 1) {
                    return 0;
                }
                double v = Math.cos(Math.PI * u / 2);
                return Math.PI * v / 4;
            }
            case ("logistic"): {
                return 1 / (Math.exp(u) + 2 + Math.exp(-u));
            }
            case ("sigmoid"): {
                return 2 / (Math.PI * (Math.exp(u) + Math.exp(-u)));
            }
            default:
                return 0;
        }
    }

}