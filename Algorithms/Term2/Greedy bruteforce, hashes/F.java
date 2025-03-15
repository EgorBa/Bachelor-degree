import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class F {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("john.in"));
        String[] split = in.readLine().split("[\\s]");
        int n = Integer.parseInt(split[0]);
        int[][] a = new int[n][2];
        for (int i = 0; i < n; i++) {
            split = in.readLine().split("[\\s]");
            a[i][0] = Integer.parseInt(split[0]);
            a[i][1] = Integer.parseInt(split[1]);
        }
        in.close();
        Arrays.sort(a, Comparator.comparingInt(arr -> arr[1]));
        Arrays.sort(a, Comparator.comparingInt(arr -> arr[0]));
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = a[i][1];
        }
        PrintWriter out = new PrintWriter("john.out");
        out.println(numberOfInversions(b));
        out.close();
    }

    private static long numberOfInversions(int[] data) {
        int[] temp = new int[data.length];
        return mergeSort(data, temp, 0, data.length - 1);
    }

    private static long mergeSort(int[] data, int[] temp, int low, int high) {
        long inversions = 0L;
        if (high > low) {
            int mid = (high + low) / 2;
            inversions = mergeSort(data, temp, low, mid);
            inversions += mergeSort(data, temp, mid + 1, high);
            inversions += merge(data, temp, low, mid + 1, high);
        }
        return inversions;
    }

    private static long merge(int[] data, int[] temp, int low, int mid, int high) {
        int i, j, k = 0;
        long invertions = 0L;
        i = low;
        j = mid;
        k = low;
        while (i <= (mid - 1) && j <= high) {
            if (data[i] <= data[j]) {
                temp[k++] = data[i++];
            } else {
                temp[k++] = data[j++];

                invertions += (mid - i);
            }
        }
        while (i <= (mid - 1)) {
            temp[k++] = data[i++];
        }
        while (j <= high) {
            temp[k++] = data[j++];
        }
        for (i = low; i <= high; i++) {
            data[i] = temp[i];
        }
        return invertions;
    }

}
