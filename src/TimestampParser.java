package src;

public class TimestampParser {
    String datePart;
    String timePart;
    Character datedelimiter;
    Character timedelimiter;
    int day;
    int month;
    int year;
    int hour;
    int minute;
    int second;


    public TimestampParser(String timestamp) {

        this.datePart = timestamp.split(" ")[0]; // "2023-10-05"
        this.timePart =  timestamp.split(" ")[1]; // "14:30:00"
        this.datedelimiter = getDeliminer(this.datePart);
        this.timedelimiter = getDeliminer(this.timePart);
        this.parseTimestamp();

    }

    public Character getDeliminer(String part) {
        if (part.contains("/")){
            return '/';
        } else if (part.contains(":")) {
            return ':';
        } else if (part.contains("-")) {
            return '-';
        }
        return null;
    }

    private void parseTimestamp() {
        String[] dateParts = this.datePart.split(String.valueOf(this.datedelimiter));
        this.day = Integer.parseInt(dateParts[2]);
        this.month = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[0]);

        String[] timeParts = this.timePart.split(String.valueOf(this.timedelimiter));
        this.hour = Integer.parseInt(timeParts[0]);
        this.minute = Integer.parseInt(timeParts[1]);
        this.second = Integer.parseInt(timeParts[2]);
    }


    public int getDay() {
        return day;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public int getSecond() {
        return second;
    }




}
