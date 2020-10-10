package sample;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String currentDateToStr(){
        Date currentDate = new Date();
        Date banDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*
        System.out.println("currntDate " + dateFormat.format(currentDate));
        System.out.println("banDate " + dateFormat.format(banDate));

        if (currentDate.getTime() > banDate.getTime()){
            System.out.println("Currentdate позже");
        } else {
            System.out.println("bandate позже");
        }

         */
        return dateFormat.format(currentDate);
    }
    public static long currentDate(){
        Date current = new Date();
        return current.getTime();
    }
}
