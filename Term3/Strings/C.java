import java.io.*;

public class C {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        int[] z = new int[str.length()];
        in.close();
        for (int i = 0; i < str.length(); i++) {
            z[i] = 0;
        }
        int l = 0, r = 0;
        for (int i = 1; i < str.length(); i++) {
            z[i] = Math.max(0, Math.min(r - i, z[i - l]));
            while (i + z[i] < str.length() && str.charAt(z[i]) == str.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 1; i < str.length(); i++) {
            out.write(z[i] + " ");
        }
        out.close();
    }
}