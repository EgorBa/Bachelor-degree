import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class A {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Integer> list = new ArrayList();
        while (reader.ready()) {
            String[] split = reader.readLine().split("[\\s]");
            switch (split[0]) {
                case ("insert"):
                    boolean flag = false;
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) == Integer.parseInt(split[1])) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        list.add(Integer.parseInt(split[1]));
                    }
                    break;
                case ("exists"):
                    flag = false;
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) == Integer.parseInt(split[1])) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        System.out.println("true");
                    } else {
                        System.out.println("false");
                    }
                    break;
                case ("delete"):
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) == Integer.parseInt(split[1])) {
                            list.remove(j);
                        }
                    }
                    break;
                case ("next"):
                    int min = Integer.MAX_VALUE;
                    for (int j = 0; j < list.size(); j++) {
                        if ((list.get(j) > Integer.parseInt(split[1])) && (list.get(j) < min)) {
                            min = list.get(j);
                        }
                    }
                    if (min == Integer.MAX_VALUE) {
                        System.out.println("none");
                    } else {
                        System.out.println(min);
                    }
                    break;
                case ("prev"):
                    int max = Integer.MIN_VALUE;
                    for (int j = 0; j < list.size(); j++) {
                        if ((list.get(j) < Integer.parseInt(split[1])) && (list.get(j) > max)) {
                            max = list.get(j);
                        }
                    }
                    if (max == Integer.MIN_VALUE) {
                        System.out.println("none");
                    } else {
                        System.out.println(max);
                    }
                    break;
            }
        }
    }
}