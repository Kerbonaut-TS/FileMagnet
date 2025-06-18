package src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimestampParser {

    boolean isValid = false;
    private Calendar calendar;
    public Date timestamp;
    String formats[] = {
            "yyyy:MM:dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "MM-dd-yyyy HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss"
    };

    public TimestampParser(String timestamp) {

        //try to parse the string timestamp with a list of possible formats
        if (timestamp == null || timestamp.isEmpty()) {
            isValid = false;
        }
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                this.timestamp = sdf.parse(timestamp);
                isValid = true;
                System.out.println("Valid timestamp: " + this.timestamp + " with format: " + format);
            } catch (Exception e) {
                // Continue to the next format if parsing fails
                System.out.println("Invalid timestamp: " + timestamp );
            }
        }
        if(isValid && this.timestamp != null) {
            this.calendar = Calendar.getInstance();
            calendar.setTime(this.timestamp);
        }


    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);

    }
    public int getMonth() {
        return calendar.get(Calendar.MONTH);

    }
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }
    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }
    public Date get_timestamp() {
        return this.timestamp;
    }




}
