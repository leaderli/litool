package io.leaderli.litool.core.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
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
     * @param date         字符串格式的日期
     * @param beforeFormat 原始日期的格式
     * @param afterFormat  转换后日期的格式
     * @return 转换后的日期字符串
     */
    public static String parse(String date, String beforeFormat, String afterFormat) {

        SimpleDateFormat before = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat after = new SimpleDateFormat(afterFormat);


        try {
            return after.format(before.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 示例：
     * <pre>
     *     DateUtil.between("HHmm","0800","2200")
     * </pre>
     *
     * @param format 时间格式
     * @param before format格式的起始时间
     * @param after  format格式的结束时间
     * @return 判断当时时间是否在时间范围内，即大于等于起始时间，小于结束时间
     * @see #between(String, String, String, String)
     */

    public static boolean between(String format, String before, String after) {

        return between(format, now(format), before, after);
    }

    /**
     * 测试啊啊
     * 示例：
     * <pre>
     *     DateUtil.between("HHmm","0759","0800","2200")
     * </pre>
     *
     * @param format  时间格式
     * @param between format格式的比较时间
     * @param before  format格式的起始时间
     * @param after   format格式的结束时间
     * @return 判断当时时间是否在时间范围内
     */
    public static boolean between(String format, String between, String before, String after) {

        Date nowDate = parse(between, format);
        Date beforeDate = parse(before, format);
        Date afterDate = parse(after, format);
        return beforeDate.compareTo(nowDate) <= 0 && afterDate.compareTo(nowDate) > 0;
    }

    /**
     * @param dateStr 字符串格式的日期
     * @param format  日期的格式
     * @return 日期
     */
    public static Date parse(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calc(String dateStr, String format, int field, int cacl) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = parse(dateStr, simpleDateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, cacl);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * @param dateStr 字符串格式的日期
     * @param format  日期的格式
     * @return 日期
     */
    public static Date parse(String dateStr, SimpleDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
