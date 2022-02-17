import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        SearchAggregator s = new SearchAggregator();
        System.out.println(s.getRequest("football", new RealServer()));
        System.out.println(s.getRequest("sport", new RealServer()));
        System.exit(0);
    }

    private static class RealServer implements Server {

        @Override
        public ArrayList<String> getRequest(String query, ServerType serverType) {
            if (query == null || query.isBlank()) return new ArrayList<>();
            String server;
            switch (serverType) {
                case GOOGLE -> server = "https://www.google.com/search?q=";
                case YANDEX -> server = "https://www.yandex.ru/search/?text=";
                case BING -> server = "https://www.bing.com/search?q=";
                default -> server = "";
            }
            if (server.isBlank()) return new ArrayList<>();
            String charset = "UTF-8";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.174 YaBrowser/22.1.2.834 Yowser/2.5 Safari/537.36"; // Change this to your company's name and bot homepage!

            Document doc = null;
            try {
                doc = Jsoup.connect(server + URLEncoder.encode(query, charset)).userAgent(userAgent).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc == null) return new ArrayList<>();
            return extractLinks(doc.body().text());
        }
    }

    private static ArrayList<String> extractLinks(String body) {
        String[] strs = body.split("[\\s]");
        HashSet<String> answers = new HashSet<>();
        for (String str : strs) {
            if ((str.startsWith("https:")
                    || str.endsWith(".ru")
                    || str.endsWith(".com")
                    || str.endsWith(".org"))
                    && !strContainsUpperChars(str)) {
                answers.add(str);
            }
            if (answers.size() == 5) {
                break;
            }
        }
        return new ArrayList<>(answers);
    }

    private static boolean strContainsUpperChars(String str) {
        boolean flag = false;
        for (Character chr : str.toCharArray()) {
            flag = flag || Character.isUpperCase(chr);
        }
        return flag;
    }
}
