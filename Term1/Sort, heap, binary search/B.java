import java.util.Scanner;

public class B {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int[] a = new int[101];
        String s = scan.nextLine();
        String[] splits = s.split(" ");
        for (int i = 0; i < splits.length; i++)
            try {
                a[Integer.parseInt(splits[i])] = a[Integer.parseInt(splits[i])] + 1;
            } catch (Exception e) {
            }
        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < a[i]; j++) {
                System.out.print(i + " ");
            }
        }
    }

}