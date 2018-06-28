package com.brunogtavares.mycalendar.MobileFrontEnd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by brunogtavares on 6/28/18.
 */

public class DateUtils {
    // Format: Thu Jun 28 10:28:00 EDT 2018
    // private static final String DATE_FORMAT = "MMM dd, yyyy";
    // private static final String TIME_FORMAT = "HH:mm";
    // private static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    private static final String DATETIME_FORMAT = "EEE MMM dd h:m a yyyy";

    public static String getDay(Date date) throws ParseException {
        return new SimpleDateFormat("yyyy").format(date);
    };

    public static String getWeek(Date date) {
        return new SimpleDateFormat("EEE").format(date);
    }

    public static String getTime(String time) {
        return new SimpleDateFormat("H:mm a").format(time);
    }

}
