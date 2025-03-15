import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class E {

    public static void main(String[] args) throws IOException {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        String[] s = scan.readLine().split("[\\s]");
        Stack<Long> st = new Stack<>();
        long k = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i].equals("*")) {
                k = st.pop() * st.pop();
                st.push(k);
            }
            if (s[i].equals("+")) {
                k = st.pop() + st.pop();
                st.push(k);
            }
            if (s[i].equals("-")) {
                k = -st.pop() + st.pop();
                st.push(k);
            }
            if (!s[i].equals("-") && !s[i].equals("+") && !s[i].equals("*")) {
                st.push(Long.parseLong(s[i]));
            }
        }
        System.out.println(st.pop());
    }
}