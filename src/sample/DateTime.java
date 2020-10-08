package sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String currentDate(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat();
        return dateFormat.format(currentDate);
    }
}
