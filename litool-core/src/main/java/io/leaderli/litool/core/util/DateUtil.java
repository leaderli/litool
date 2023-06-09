package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        return after.format(RuntimeExceptionTransfer.apply(before::parse, date));

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
     * @return 判断当时时间是否在时间范围内
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
        return beforeDate.before(nowDate) && afterDate.after(nowDate);
    }

    /**
     * @param dateStr 字符串格式的日期
     * @param format  日期的格式
     * @return 日期
     */
    public static Date parse(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return RuntimeExceptionTransfer.apply(simpleDateFormat::parse, dateStr);

    }
}
