package API;

import Utils.NetworkUtils;

import java.net.URL;

public class APIUtilsVK implements APIUtils {

    @Override
    public int[] getHoursArray(int hours, String hashtag) {
        assert 1 <= hours && hours <= 24 && hashtag != null && !hashtag.isBlank() : "Incorrect parameters";
        int[] array = new int[hours];
        for (int i = 0; i < hours; i++) {
            array[i] = getTagsInHour(i, hashtag);
        }
        return array;
    }

    int getTagsInHour(int i, String hashtag) {
        URL s = NetworkUtils.generatedURL(hashtag, i);
        if (s != null) {
            String response = NetworkUtils.getResponseFromURL(s);
            if (response != null) {
                return NetworkUtils.getCountFromResponse(response);
            }
        }
        return 0;
    }
}
