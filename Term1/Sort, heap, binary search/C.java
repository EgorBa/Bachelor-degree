import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class C {

    private static void quickSort(int[] array, int low, int high) {
        if (array.length == 0)
            return;//завершить выполнение если длина массива равна 0

        if (low >= high)
            return;//завершить выполнение если уже нечего делить

        // выбрать опорный элемент
        int middle = low + (high - low) / 2;
        int opora = array[middle];

        // разделить на подмассивы, который больше и меньше опорного элемента
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < opora) {
                i++;
            }

            while (array[j] > opora) {
                j--;
            }

            if (i <= j) {//меняем местами
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        // вызов рекурсии для сортировки левой и правой части
        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }


    public static long in = 0;

    public static void msort(int[] A, int p, int r) {
        if (p < r) {
            int q = (int) Math.floor((r + p) / 2);
            msort(A, p, q);
            msort(A, q + 1, r);
            merge(A, p, q, r);
        }

    }

    private static void merge(int[] A, int p, int q, int r) {

        int n1 = q - p + 1; // number of elements in left array
        int n2 = (r - q);
        int[] L = new int[n1 + 1];
        int[] R = new int[n2 + 1];
        for (int i = 0; i < n1; i++) {
            L[i] = A[p + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = A[q + j + 1];
        }
        L[n1] = Integer.MAX_VALUE;
        R[n2] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        for (int k = p; k < (r + 1); k++) {
            if (L[i] <= R[j]) {
                A[k] = L[i];
                i = i + 1;
            } else {
                A[k] = R[j];
                j = j + 1;
                int numOFElements = n1 - i;
                in = in + numOFElements;
                //inversions=inversions+numOFElements; // this counts inversion
            }
        }
    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            a[i] = scan.nextInt();
            b[i] = a[i];
        }
        long count = 0;
        int y = 0;
        msort(a, 0, a.length - 1);
        System.out.print(in);
    }

}