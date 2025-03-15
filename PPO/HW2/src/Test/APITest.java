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
    APIUtils APIUtils;
    NetworkUtils stubNetworkUtils;
    int[] expectedArray = {0, 1, 2, 3, 4, 5};

    @Before
    public void prepare() throws MalformedURLException {
        stubNetworkUtils = Mockito.mock(NetworkUtils.class);
        for (int i = 0; i < expectedArray.length; i++) {
            URL url = new URL("https://test.ru" + i);
            Mockito.when(stubNetworkUtils.generatedURL(HASHTAG, i)).thenReturn(url);
            Mockito.when(stubNetworkUtils.getResponseFromURL(url)).thenReturn(RESPONSE + i);
            Mockito.when(stubNetworkUtils.getCountFromResponse(RESPONSE + i)).thenReturn(i);
        }
        APIUtils = new APIUtilsVK(stubNetworkUtils);
    }

    @Test
    public void testGetArray() {
        int[] array = APIUtils.getHoursArray(expectedArray.length, HASHTAG);
        Mockito.verify(stubNetworkUtils, Mockito.times(expectedArray.length)).getCountFromResponse(Mockito.anyString());
        Mockito.verify(stubNetworkUtils, Mockito.times(expectedArray.length)).generatedURL(Mockito.anyString(), Mockito.anyInt());
        assertEquals(Arrays.toString(array), Arrays.toString(expectedArray));
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectLength() {
        APIUtils.getHoursArray(0, HASHTAG);
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectHashtag() {
        APIUtils.getHoursArray(expectedArray.length, null);
    }

}
