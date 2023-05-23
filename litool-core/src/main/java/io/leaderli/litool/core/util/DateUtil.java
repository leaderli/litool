package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {


    /**
     * @param format 日期的格式
     * @return 格式化后的日期字符串
     * @see DateTimeFormatter
     * @see LocalDateTime#now()
     */
    public static String now(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }


    /**
     * @param format        日期的格式
     * @param localDateTime 日期
     * @return 格式化后的日期字符串
     * @see DateTimeFormatter
     */

    public static String format(String format, LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    /**
     * @param format 日期的格式
     * @param date   日期
     * @return 格式化后的日期字符串
     * @see DateTimeFormatter
     * @see LocalDateTime
     */

    public static String format(String format, Date date) {
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DateTimeFormatter.ofPattern(format).format(localDate);
    }


    /**
     * @param dateStr 字符串格式的日期
     * @param format  日期的格式
     * @return 日期
     */
    public static Date parse(String dateStr, String format) {
        return RuntimeExceptionTransfer.apply(new SimpleDateFormat(format)::parse, dateStr);

    }

    /**
     * @param date         字符串格式的日期
     * @param beforeFormat 原始日期的格式
     * @param afterFormat  转换后日期的格式
     * @return 转换后的日期字符串
     */
    public static String parse(String date, String beforeFormat, String afterFormat) {

        SimpleDateFormat before = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat after = new SimpleDateFormat(afterFormat);

        return after.format(RuntimeExceptionTransfer.apply(before::parse, date));

    }

}
