import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class G {
    private static int n = 0;
    private static int m = 0;
    private static int mColor = 0;
    private static int[] tin;
    private static int[] tout;
    private static int time = -1;
    private static ArrayList[] array;
    private static ArrayList[] newarray;
    private static ArrayList[] nearray;
    private static int[] colors;
    private static int[] used;
    private static int[] sort;
    private static Stack<Integer> stack = new Stack<>();
    private static ArrayList<String> list = new ArrayList<>();
    private static HashSet<String> ans = new HashSet<>();
    private static HashMap<Integer, String> names = new HashMap<>();
    private static HashMap<String, Integer> map = new HashMap<>();
    private static HashMap<Integer, Integer> map1 = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] split = in.readLine().split("[\\s]");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        array = new ArrayList[2 * n];
        nearray = new ArrayList[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            array[i] = new ArrayList();
            nearray[i] = new ArrayList();
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            String str = in.readLine();
            map.put(str, count);
            names.put(count, str);
            names.put(count + n, str);
            count++;
        }
        for (int i = 0; i < m; i++) {
            split = in.readLine().split("[\\s]");
            int parent, child;
            if (split[0].charAt(0) == '-') {
                parent = map.get(split[0].substring(1)) + n;
            } else {
                parent = map.get(split[0].substring(1));
            }
            if (split[2].charAt(0) == '-') {
                child = map.get(split[2].substring(1)) + n;
            } else {
                child = map.get(split[2].substring(1));
            }
            array[parent].add(child);
            array[(child + n) % (2 * n)].add((parent + n) % (2 * n));
            nearray[child].add(parent);
            nearray[(parent + n) % (2 * n)].add((child + n) % (2 * n));
        }
        used = new int[2 * n];
        colors = new int[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            if (used[i] == 0) {
                dfs1(i);
            }
        }
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (colors[u] == 0) {
                mColor++;
                dfs2(u);
            }
        }
        boolean flag = false;
        for (int i = 0; i < n; i++) {
            if (colors[i] == colors[i + n]) {
                flag = true;
                break;
            }
        }
        if (flag) {
            System.out.println(-1);
        } else {
            for (int i = 0; i < 2 * n; i++) {
                for (int j = 0; j < array[i].size(); j++) {
                    int u = (Integer) array[i].get(j);
                    if (colors[i] == colors[u]) {
                        continue;
                    }
                    ans.add((colors[i] - 1) + " " + (colors[u] - 1));
                }
            }
            newarray = new ArrayList[mColor];
            for (int i = 0; i < mColor; i++) {
                newarray[i] = new ArrayList();
            }
            while (!ans.isEmpty()) {
                String str = ans.iterator().next();
                split = str.split("[\\s]");
                int parent, child;
                parent = Integer.parseInt(split[0]);
                child = Integer.parseInt(split[1]);
                newarray[parent].add(child);
                ans.remove(str);
            }
            tin = new int[mColor];
            tout = new int[mColor];
            used = new int[mColor];
            for (int i = 0; i < mColor; i++) {
                if (used[i] == 0) {
                    dfs(i);
                }
            }
            sort = new int[2 * mColor];
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < mColor; i++) {
                sort[tout[i]]++;
                map.put(tout[i], i);
            }
            count = 0;
            for (int i = 2 * mColor - 1; i >= 0; i--) {
                if (sort[i] == 1) {
                    map1.put(map.get(i), count);
                    count++;
                }
            }
            for (int i = 0; i < n; i++) {
                if (map1.get(colors[i] - 1) > map1.get(colors[i + n] - 1)) {
                    list.add(names.get(i));
                }
            }
            System.out.println(list.size());
            for (String str : list) {
                System.out.println(str);
            }
        }
    }

    private static void dfs1(int v) {
        used[v] = 1;
        for (int i = 0; i < array[v].size(); i++) {
            int u = (Integer) array[v].get(i);
            if (used[u] == 0) {
                dfs1(u);
            }
        }
        stack.push(v);
    }

    private static void dfs2(int v) {
        colors[v] = mColor;
        for (int i = 0; i < nearray[v].size(); i++) {
            int u = (Integer) nearray[v].get(i);
            if (colors[u] == 0) {
                dfs2(u);
            }
        }
    }

    private static void dfs(int v) {
        used[v] = 1;
        time++;
        tin[v] = time;
        for (int i = 0; i < newarray[v].size(); i++) {
            int u = (Integer) newarray[v].get(i);
            if (used[u] != 1) {
                if (used[u] == 0) {
                    dfs(u);
                }
            }
        }
        used[v] = 2;
        time++;
        tout[v] = time;
    }

}
