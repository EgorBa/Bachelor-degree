package Test;

import API.APIUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

public class APITest {

    APIUtils stubAPIUtils;
    int[] expectedArray = {0, 1, 2, 3, 4, 5};

    @Before
    public void prepare() {
        stubAPIUtils = Mockito.mock(APIUtils.class);
        Mockito.when(stubAPIUtils.getHoursArray(expectedArray.length, "hashtag")).thenReturn(expectedArray);
        Mockito.when(stubAPIUtils.getHoursArray(expectedArray.length, null)).thenThrow(new AssertionError());
        Mockito.when(stubAPIUtils.getHoursArray(0, "hashtag")).thenThrow(new AssertionError());
    }

    @Test
    public void testGetArray() {
        int[] array = stubAPIUtils.getHoursArray(expectedArray.length, "hashtag");
        Mockito.verify(stubAPIUtils, Mockito.times(1)).getHoursArray(expectedArray.length, "hashtag");
        assertEquals(array, expectedArray);
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectLength() {
        stubAPIUtils.getHoursArray(0, "hashtag");
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectHashtag() {
        stubAPIUtils.getHoursArray(expectedArray.length, null);
    }

}
