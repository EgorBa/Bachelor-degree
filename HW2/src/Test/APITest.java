package Test;

import API.APIUtils;
import API.APIUtilsVK;
import Utils.NetworkUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class APITest {

    final String RESPONSE = "response";
    final String HASHTAG = "hashtag";
    APIUtils stubAPIUtils;
    NetworkUtils networkUtils;
    int[] expectedArray = {0, 1, 2, 3, 4, 5};

    @Before
    public void prepare() throws MalformedURLException {
        networkUtils = Mockito.mock(NetworkUtils.class);
        for (int i = 0; i < expectedArray.length; i++) {
            URL url = new URL("https://test.ru" + i);
            Mockito.when(networkUtils.generatedURL(HASHTAG, i)).thenReturn(url);
            Mockito.when(networkUtils.getResponseFromURL(url)).thenReturn(RESPONSE + i);
            Mockito.when(networkUtils.getCountFromResponse(RESPONSE + i)).thenReturn(i);
        }
        stubAPIUtils = new APIUtilsVK(networkUtils);
    }

    @Test
    public void testGetArray() {
        int[] array = stubAPIUtils.getHoursArray(expectedArray.length, HASHTAG);
        System.out.println(Arrays.toString(array));
        Mockito.verify(networkUtils, Mockito.times(expectedArray.length)).getCountFromResponse(Mockito.anyString());
        assertEquals(Arrays.toString(array), Arrays.toString(expectedArray));
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectLength() {
        stubAPIUtils.getHoursArray(0, HASHTAG);
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectHashtag() {
        stubAPIUtils.getHoursArray(expectedArray.length, null);
    }

}
