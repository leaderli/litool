package io.leaderli.litool.core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {


    /**
     * @param format -
     * @return formatted date  of now()
     * @see DateTimeFormatter
     * @see LocalDate#now()
     */
    public static String format(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }

    /**
     * @param format    -
     * @param localDate -
     * @return formatted localDate
     */

    public static String format(String format, LocalDate localDate) {
        return DateTimeFormatter.ofPattern(format).format(localDate);
    }

    /**
     * @param format        -
     * @param localDateTime -
     * @return formatted localDateTime
     */

    public static String format(String format, LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    /**
     * @param format -
     * @param date   -
     * @return formatted date
     */

    public static String format(String format, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return DateTimeFormatter.ofPattern(format).format(localDate);
    }


    /**
     * @param format  -
     * @param dateStr -
     * @return a localDate parse from string
     */
    public static LocalDate parser2LocalDate(String format, String dateStr) {
        return LocalDate.from(DateTimeFormatter.ofPattern(format).parse(dateStr));
    }


    /**
     * @param format  -
     * @param dateStr -
     * @return a localDateTime parse from string
     */
    public static LocalDateTime parser2LocalDateTime(String format, String dateStr) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(dateStr));
    }


    /**
     * @param format  -
     * @param dateStr -
     * @return a date parse from string
     */
    public static Date parser2Date(String format, String dateStr) {

        LocalDateTime from = LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(dateStr));

        return Date.from(Instant.from(from));


    }
}
