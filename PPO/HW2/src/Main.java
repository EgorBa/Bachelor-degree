import API.APIUtils;
import API.APIUtilsVK;

import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        APIUtils apiUtils = new APIUtilsVK();
        System.out.println(Arrays.toString(apiUtils.getHoursArray(10,"cats")));
        System.out.println(Arrays.toString(apiUtils.getHoursArray(3,"dogs")));
    }
}
