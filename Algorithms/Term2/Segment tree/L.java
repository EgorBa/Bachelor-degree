import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class L {
    private static int[][][] t;
    public static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.valueOf(reader.readLine());
        String[] split;
        t = new int[n][n][n];
        while (reader.ready()) {
            split = reader.readLine().split("[\\s]");
            switch (split[0]) {
                case ("1"):
                    inc(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
                    break;
                case ("2"):
                    int sum1 = sum(Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6])) - sum(Integer.parseInt(split[1]) - 1, Integer.parseInt(split[5]), Integer.parseInt(split[6])) - sum(Integer.parseInt(split[4]), Integer.parseInt(split[2]) - 1, Integer.parseInt(split[6])) + sum(Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]) - 1, Integer.parseInt(split[6]));
                    int sum2 = sum(Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[3]) - 1) - sum(Integer.parseInt(split[1]) - 1, Integer.parseInt(split[5]), Integer.parseInt(split[3]) - 1) - sum(Integer.parseInt(split[4]), Integer.parseInt(split[2]) - 1, Integer.parseInt(split[3]) - 1) + sum(Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]) - 1, Integer.parseInt(split[3]) - 1);
                    System.out.println(sum1 - sum2);
                    break;
                default:
                    break;
            }
        }
    }

    private static int sum(int x, int y, int z) {
        int result = 0;
        for (int i = x; i >= 0; i = (i & (i + 1)) - 1) {
            for (int j = y; j >= 0; j = (j & (j + 1)) - 1) {
                for (int k = z; k >= 0; k = (k & (k + 1)) - 1) {
                    result += t[i][j][k];
                }
            }
        }
        return result;
    }

    private static void inc(int x, int y, int z, int data) {
        for (int i = x; i < n; i = (i | (i + 1))) {
            for (int j = y; j < n; j = (j | (j + 1))) {
                for (int k = z; k < n; k = (k | (k + 1))) {
                    t[i][j][k] += data;
                }
            }
        }
    }
}