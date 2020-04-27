import java.util.*;

public class F {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        List<Integer> sk = new LinkedList<>();
        Stack<Integer> pop = new Stack<>();
        Stack<Integer> push = new Stack<>();
        Stack<Integer> check = new Stack<>();
        Stack<String> ans = new Stack<>();
        sk.add(scan.nextInt());
        check.add(sk.get(0));
        int count = 1;
        for (int i = 1; i < n; i++) {
            sk.add(scan.nextInt());
            check.add(sk.get(i));
            if (sk.get(i - 1) <= sk.get(i)) {
                count++;
            }
        }
        if (count == sk.size()) {
            Collections.sort(sk);
            for (Integer l : sk) {
                System.out.println("push");
                System.out.println("pop");
            }
        } else {
            int i = 0;
            push.push(sk.get(0));
            sk.remove(0);
            ans.push("push");
            while (!sk.isEmpty()) {
                if (push.isEmpty() || push.peek() >= sk.get(0)) {
                    push.push(sk.get(0));
                    ans.push("push");
                    sk.remove(0);
                    i++;
                } else {
                    pop.push(push.pop());
                    ans.push("pop");
                    i++;
                    if (i != 0) {
                        i--;
                    } else {
                        push.push(sk.get(0));
                        sk.remove(0);
                        ans.push("push");
                    }
                }
            }
            while (!push.isEmpty()) {
                pop.push(push.pop());
                ans.push("pop");
            }
            Collections.sort(check);
            int cnt = 0;
            for (int l = 0; l < check.size(); l++) {
                if (!pop.get(l).equals(check.get(l))) {
                    cnt++;
                }
            }
            if (cnt > 0) {
                System.out.println("impossible");
            } else {
                for (String o : ans) {
                    System.out.println(o);
                }
            }
        }
    }
}