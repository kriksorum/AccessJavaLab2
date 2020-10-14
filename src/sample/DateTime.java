package sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String currentDateToStr(){
        Date currentDate = new Date();
        Date banDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateFormat.format(currentDate);
    }
    public static long currentDate(){
        Date current = new Date();
        return current.getTime();
    }
}
