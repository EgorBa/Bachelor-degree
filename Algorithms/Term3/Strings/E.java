import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class E {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        int p = 1;
        int i = 1;
        while (i < str.length()) {
            for (int j = 0; j < p; j++) {
                if (i >= str.length()) {
                    p = str.length();
                    break;
                }
                if (str.charAt(j) == str.charAt(i)) {
                    i++;
                } else {
                    p = i + 1;
                    i++;
                    break;
                }
            }
        }
        System.out.print(p);
    }
}