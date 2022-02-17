import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;

public class SearchTests {

    ArrayList<String> googleAnswers;
    final String GOOGLE = "Google";
    int googleSleep = 0;
    ArrayList<String> yandexAnswers;
    final String YANDEX = "Yandex";
    int yandexSleep = 0;
    ArrayList<String> bingAnswers;
    final String BING = "Bing";
    int bingSleep = 0;
    SearchAggregator searchAggregator;

    @Before
    public void init() {
        googleAnswers = new ArrayList<>();
        googleAnswers.add(GOOGLE);
        yandexAnswers = new ArrayList<>();
        yandexAnswers.add(YANDEX);
        bingAnswers = new ArrayList<>();
        bingAnswers.add(BING);
        searchAggregator = new SearchAggregator();
        initSleep(0, 0, 0);
    }

    public void initSleep(int g, int y, int b) {
        googleSleep = g;
        yandexSleep = y;
        bingSleep = b;
    }

    public HashSet<String> makeRequest(String request) {
        HashMap<ServerType, ArrayList<String>> map = searchAggregator.getRequest(request, new TestServer());
        HashSet<String> answers = new HashSet<>();
        answers.addAll(map.get(ServerType.GOOGLE));
        answers.addAll(map.get(ServerType.YANDEX));
        answers.addAll(map.get(ServerType.BING));
        return answers;
    }

    @Test
    public void testAllFastServers() {
        HashSet<String> answers = makeRequest("request");
        HashSet<String> expectedAnswers = new HashSet<>();
        expectedAnswers.addAll(googleAnswers);
        expectedAnswers.addAll(yandexAnswers);
        expectedAnswers.addAll(bingAnswers);
        assertEquals(expectedAnswers, answers);
    }

    @Test
    public void testServersWithOneSleep() {
        initSleep(SearchAggregator.TIMEOUT * 2, 0, 0);
        HashSet<String> answers = makeRequest("request");
        HashSet<String> expectedAnswers = new HashSet<>();
        expectedAnswers.addAll(yandexAnswers);
        expectedAnswers.addAll(bingAnswers);
        assertEquals(expectedAnswers, answers);
    }

    @Test
    public void testAllServersSleep() {
        initSleep(SearchAggregator.TIMEOUT * 2, SearchAggregator.TIMEOUT * 3, SearchAggregator.TIMEOUT * 5);
        HashSet<String> answers = makeRequest("request");
        HashSet<String> expectedAnswers = new HashSet<>();
        assertEquals(expectedAnswers, answers);
    }

    @Test
    public void testNullRequest() {
        HashSet<String> answers = makeRequest(null);
        HashSet<String> expectedAnswers = new HashSet<>();
        assertEquals(expectedAnswers, answers);
    }

    @Test
    public void testEmptyRequest() {
        HashSet<String> answers = makeRequest("");
        HashSet<String> expectedAnswers = new HashSet<>();
        assertEquals(expectedAnswers, answers);
    }

    private class TestServer implements Server {

        @Override
        public ArrayList<String> getRequest(String query, ServerType serverType) {
            if (query == null || query.isBlank()) return new ArrayList<>();
            switch (serverType) {
                case GOOGLE -> {
                    try {
                        Thread.sleep(googleSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return googleAnswers;
                }
                case YANDEX -> {
                    try {
                        Thread.sleep(yandexSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return yandexAnswers;
                }
                case BING -> {
                    try {
                        Thread.sleep(bingSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return bingAnswers;
                }
            }
            return new ArrayList<>();
        }
    }

}
