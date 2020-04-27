import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class J {
    private static int n = 0;
    private static int m = 0;
    private static ArrayList[] array;
    private static ArrayList[] weights = new ArrayList[100001];
    private static int[] p;
    private static int[] rank;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        p = new int[n];
        rank = new int[n];
        long MST = 0;
        array = new ArrayList[n];
        for (int i = 0; i < 100001; i++) {
            weights[i] = new ArrayList();
        }
        for (int i = 0; i < n; i++) {
            p[i] = i;
            rank[i] = 0;
            array[i] = new ArrayList();
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child, weight;
            parent = Integer.parseInt(split[0]) - 1;
            child = Integer.parseInt(split[1]) - 1;
            weight = Integer.parseInt(split[2]);
            weights[weight].add(parent + " " + child);
            array[parent].add(child);
        }
        for (int i = 0; i < 100001; i++) {
            for (int j = 0; j < weights[i].size(); j++) {
                String str = (String) weights[i].get(j);
                split = str.split("[\\s]");
                int parent, child;
                parent = Integer.parseInt(split[0]);
                child = Integer.parseInt(split[1]);
                if (find(parent) != find(child)) {
                    MST += i;
                    union(parent, child);
                }
            }
        }
        System.out.println(MST);
    }

    private static int find(int x) {
        if (x == p[x]) {
            return x;
        } else {
            p[x] = find(p[x]);
            return p[x];
        }
    }

    private static void union(int x, int y) {
        if ((x = find(x)) == (y = find(y)))
            return;
        if (rank[x] < rank[y])
            p[x] = y;
        else {
            p[y] = x;
            if (rank[x] == rank[y])
                ++rank[x];
        }
    }
}