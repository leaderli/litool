package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {


    /**
     * @see DateTimeFormatter
     * @see LocalDateTime#now()
     */
    public static String now(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }


    /**
     * @see DateTimeFormatter
     */

    public static String format(String format, LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    /**
     * @see DateTimeFormatter
     * @see LocalDateTime
     */

    public static String format(String format, Date date) {
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DateTimeFormatter.ofPattern(format).format(localDate);
    }


    /**
     * @param dateStr -
     * @param format  -
     * @return a date parse from string
     */
    public static Date parse(String dateStr, String format) {
        return RuntimeExceptionTransfer.apply(new SimpleDateFormat(format)::parse, dateStr);

    }

    /**
     * @param date         -
     * @param beforeFormat -
     * @param afterFormat  -
     * @return -
     */
    public static String parse(String date, String beforeFormat, String afterFormat) {

        SimpleDateFormat before = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat after = new SimpleDateFormat(afterFormat);

        return after.format(RuntimeExceptionTransfer.apply(before::parse, date));

    }

}
