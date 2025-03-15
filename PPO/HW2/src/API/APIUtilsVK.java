package API;

import Utils.NetworkUtils;

import java.net.URL;

public class APIUtilsVK implements APIUtils {
    private final NetworkUtils networkUtils;

    public APIUtilsVK() {
        networkUtils = new NetworkUtils();
    }

    public APIUtilsVK(NetworkUtils networkUtils) {
        this.networkUtils = networkUtils;
    }

    @Override
    public int[] getHoursArray(int hours, String hashtag) {
        assert 1 <= hours && hours <= 24 && hashtag != null && !hashtag.isBlank() : "Incorrect parameters";
        int[] array = new int[hours];
        for (int i = 0; i < hours; i++) {
            array[i] = getTagsInHour(i, hashtag);
        }
        return array;
    }

    private int getTagsInHour(int i, String hashtag) {
        URL s = networkUtils.generatedURL(hashtag, i);
        if (s != null) {
            String response = networkUtils.getResponseFromURL(s);
            if (response != null) {
                return networkUtils.getCountFromResponse(response);
            }
        }
        return 0;
    }
}
