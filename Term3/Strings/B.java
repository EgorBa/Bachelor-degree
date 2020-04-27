import java.io.*;

public class B {
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        int[] prefix = new int[str.length()];
        in.close();
        for (int i = 0; i < str.length(); i++) {
            prefix[i] = 0;
        }
        for (int i = 1; i < str.length(); i++) {
            int k = prefix[i - 1];
            while (k > 0 && str.charAt(i) != str.charAt(k)) {
                k = prefix[k - 1];
            }
            if (str.charAt(i) == str.charAt(k)) {
                k++;
            }
            prefix[i] = k;
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < str.length(); i++) {
            out.write(prefix[i] + " ");
        }
        out.close();
    }
}