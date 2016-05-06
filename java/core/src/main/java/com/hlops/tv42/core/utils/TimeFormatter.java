package com.hlops.tv42.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tom on 1/31/16.
 */
public class TimeFormatter {
    private static Logger log = LogManager.getLogger(TimeFormatter.class);

    private static final String DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    public static long parse(String date) throws ParseException {
        if (date.length() == 20 && date.charAt(14) == ' ') {
            Date d = DATE_FORMAT.parse(date.substring(0, 15));
            String shift = date.substring(15);
            int minutes = Integer.parseInt(shift.substring(1, 3)) * 60 + Integer.parseInt(shift.substring(3));
            if (shift.charAt(0) == '-') {
                minutes = -minutes;
            }
            return new Date(d.getTime() + 60000 * minutes).getTime();
        } else {
            return DATE_FORMAT.parse(date).getTime();
        }
    }

    public static String format(long time) {
        return DATE_FORMAT.format(new Date(time));
    }
}
