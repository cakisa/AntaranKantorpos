package com.horirevens.antarankantorpos.libs;

/**
 * Created by horirevens on 1/18/17.
 */
public class MyDate {
    public static String datetimeIndo(String date) {
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String time = date.substring(11,19);
        String result = time + " - " + day +"/" +month +"/"+year;
        return  result;
    }
}
