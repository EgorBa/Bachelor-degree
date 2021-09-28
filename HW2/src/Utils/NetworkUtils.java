package Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class NetworkUtils {
    private static final String VK_API_BASE_URL = "https://api.vk.com/";
    private static final String VK_USERS_GET = "method/newsfeed.search?";
    private static final String PARAM_USERS_ID = "access_token=";
    private static final String PARAM_VERSION = "v=";
    private static final String PARAM_QUERY = "q=";
    private static final String PARAM_START_TIME = "start_time=";
    private static final String PARAM_END_TIME = "end_time=";

    public static URL generatedURL(String hashtag, int hours) {
        assert hashtag != null && !hashtag.isBlank() : "Incorrect parameters";

        URI builtUri;
        try {
            builtUri = new URI(VK_API_BASE_URL + VK_USERS_GET);
            builtUri = appendUri(builtUri.toString(), PARAM_VERSION + "5.81");
            builtUri = appendUri(builtUri.toString(), PARAM_QUERY + hashtag);
            builtUri = appendUri(builtUri.toString(), PARAM_USERS_ID + BuildConfig.ACCESS_TOKEN);
            builtUri = appendUri(builtUri.toString(), PARAM_START_TIME + getTime(true, hours));
            builtUri = appendUri(builtUri.toString(), PARAM_END_TIME + getTime(false, hours));
        } catch (URISyntaxException e) {
            System.err.println("Incorrect URI");
            e.printStackTrace();
            return null;
        }

        URL url;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            System.err.println("Incorrect URL or query parameter : " + builtUri.toString());
            e.printStackTrace();
            return null;
        }
        return url;
    }

    public static String getResponseFromURL(URL url) {
        assert url != null : "Empty URL";

        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.err.println("Can't open connection by this url : " + url);
            e.printStackTrace();
            return null;
        }

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean flag = scanner.hasNext();
            if (flag) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static int getCountFromResponse(String response) {
        assert response != null && !response.isBlank() : "Empty response";
        try {
            return new JSONObject(response).getJSONObject("response").getInt("total_count");
        } catch (JSONException e) {
            System.err.println("Incorrect JSON file from server : " + response);
            e.printStackTrace();
        }
        return 0;
    }

    private static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);
        return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(),
                oldUri.getQuery() == null ? appendQuery : oldUri.getQuery() + "&" + appendQuery, oldUri.getFragment());
    }

    private static Long getTime(boolean isStartTime, int hours) {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime - (long) (hours + (isStartTime ? 1 : 0)) * 60 * 60;
    }

}