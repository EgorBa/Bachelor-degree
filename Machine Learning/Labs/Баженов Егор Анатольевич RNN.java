import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class RNN {

    public static class Node {
        String str;
        ArrayList<Node> list;

        Node(String str) {
            this.str = str;
            list = new ArrayList<>();
        }

        @Override
        public int hashCode() {
            return str.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Node)) {
                return false;
            }
            Node node = (Node) o;
            return str.equals(node.str);
        }
    }

    private static final Node END = new Node("$");
    private static HashMap<Integer, Node> map;
    private static HashMap<String, Integer> map1;
    private static final int n = 4;
    private static final Random random = new Random();

    public static String getWord(String str) {
        if (str.length() < n) {
            System.out.println("Введите еще символов, для предсказания должно быть не менее " + n + " символов");
        }
        String subStr = str.substring(str.length() - n);
        int hash = map1.get(subStr);
        if (map.containsKey(hash)) {
            Node node = map.get(hash);
            return node.str + goAndPrint(node);
        } else {
            System.out.println("Введите еще символов для предсказания");
            return "";
        }
    }

    private static String goAndPrint(Node node) {
        int i = random.nextInt(node.list.size());
        Node next = node.list.get(i);
        if (!next.equals(END)) {
            //System.out.print(next.str.charAt(next.str.length() - 1));
            return next.str.charAt(next.str.length() - 1) + goAndPrint(next);
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("word_rus.txt"));
        ArrayList<String> words = new ArrayList<>();
        map = new HashMap<>();
        map1 = new HashMap<>();
        while (in.ready()) {
            String str = in.readLine();
            str = str.toLowerCase();
            str = str.replaceAll("\\.", "");
            str = str.replaceAll(",", "");
            str = str.replaceAll("!", "");
            str = str.replaceAll("\\?", "");
            str = str.replaceAll("'", "");
            words.add(str);
        }
        int count = 0;
        for (String s : words) {
            if (s.length() < n) continue;
            Node lastNode = null;
            for (int j = 0; j <= s.length() - n; j++) {
                String subStr = s.substring(j, j + n);
                Node node = new Node(subStr);
                map1.putIfAbsent(subStr, count);
                count++;
                int hash = map1.get(subStr);
                if (map.containsKey(hash)) {
                    node = map.get(hash);
                } else {
                    map.put(hash, node);
                }
                if (lastNode != null) {
                    lastNode.list.add(node);
                }
                lastNode = node;
            }
            if (lastNode != null) lastNode.list.add(END);
        }
        double all = 0;
        double right = 0;
        for (String s : words) {
            if (s.length() < n) continue;
            if (s.equals(getWord(s.substring(0, n)))) {
                right++;
            }
            all++;
        }
        System.out.println("Accuracy : " + (right / all) * 100);
        String str = "абза";
        System.out.print(getWord(str));
        getWord(str);
    }

}
