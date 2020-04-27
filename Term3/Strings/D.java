import java.io.*;

public class D {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String string = in.readLine();
        int start_size = string.length();
        String str = in.readLine();
        string = string + '#' + str;
        int[] z = new int[string.length()];
        in.close();
        for (int i = 0; i < string.length(); i++) {
            z[i] = 0;
        }
        int l = 0, r = 0;
        for (int i = 1; i < string.length(); i++) {
            z[i] = Math.max(0, Math.min(r - i, z[i - l]));
            while (i + z[i] < string.length() && string.charAt(z[i]) == string.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        int count = 0;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 1; i < string.length(); i++) {
            if (z[i] == start_size) {
                count++;
            }
        }
        System.out.println(count);
        for (int i = 1; i < string.length(); i++) {
            if (z[i] == start_size) {
                out.write((i - start_size) + " ");
            }
        }
        out.close();
    }
}
