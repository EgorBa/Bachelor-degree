import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class E {

    public static int levensteinInstruction(String s1, String s2, int InsertCost, int DeleteCost, int ReplaceCost) {
        int m = s1.length();
        int n = s2.length();
        int[][] D = new int[m + 1][n + 1];
        D[0][0] = 0;
        for (int j = 1; j <= n; j++) {
            D[0][j] = D[0][j - 1] + InsertCost;
        }
        for (int i = 1; i <= m; i++) {
            D[i][0] = D[i - 1][0] + DeleteCost;
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                    D[i][j] = Math.min(D[i - 1][j] + DeleteCost, Math.min(
                            D[i][j - 1] + InsertCost,
                            D[i - 1][j - 1] + ReplaceCost));
                } else {
                    D[i][j] = D[i - 1][j - 1];
                }
            }
        }
        return D[m][n];
    }

    public static void main(String s[]) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(levensteinInstruction(scan.readLine(), scan.readLine(), 1, 1, 1));
    }
}