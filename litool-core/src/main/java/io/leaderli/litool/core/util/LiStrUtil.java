package io.leaderli.litool.core.util;

import io.leaderli.litool.core.lang3.LiArrayUtils;
import io.leaderli.litool.core.lang3.LiCharSequenceUtils;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @author leaderli
 * @since 2022/6/15
 */
public class LiStrUtil {
    /**
     * @param origin     -
     * @param min_length -
     * @return -
     * @see #just(String, int, String)
     */
    public static String just(String origin, int min_length) {
        return just(origin, min_length, null);
    }


    /**
     * @param origin     原字符串
     * @param min_length 最小长度
     * @param padding    占位符
     * @return 当位数达不到最小长度时，在左右两侧补充占位符，默认占位符 -
     */
    public static String just(String origin, int min_length, String padding) {

        if (origin == null) {
            origin = "";
        }
        if (padding == null) {
            padding = "-";
        }

        if (origin.length() >= min_length) {
            return origin;
        }

        int gap = min_length - origin.length();
        if ((gap & 1) == 1) { // 奇数
            gap = gap / 2 + 1;
        } else {
            gap = gap / 2;
        }
        String replace = new String(new char[gap]).replace("\0", padding);
        return replace + origin + replace;

    }

    /**
     * @param origin     -
     * @param min_length -
     * @return -
     * @see #ljust(String, int, String)
     */
    public static String ljust(String origin, int min_length) {
        return ljust(origin, min_length, null);
    }


    /**
     * @param origin     原字符串
     * @param min_length 最小长度
     * @param padding    占位符
     * @return 当位数达不到最小长度时，在左侧补充占位符，默认占位符为空格
     */
    public static String ljust(String origin, int min_length, String padding) {

        if (origin == null) {
            origin = "";
        }
        if (padding == null) {
            padding = " ";
        }

        if (origin.length() >= min_length) {
            return origin;
        }

        return new String(new char[min_length - origin.length()]).replace("\0", padding) + origin;

    }

    /**
     * @param origin     -
     * @param min_length -
     * @return -
     * @see #rjust(String, int, String)
     */
    public static String rjust(String origin, int min_length) {
        return rjust(origin, min_length, null);
    }

    /**
     * @param origin     原字符串
     * @param min_length 最小长度
     * @param padding    占位符
     * @return 当位数达不到最小长度时，在右侧补充占位符，默认占位符为空格
     */
    public static String rjust(String origin, int min_length, String padding) {

        if (origin == null) {
            origin = "";
        }
        if (padding == null) {
            padding = " ";
        }

        if (origin.length() >= min_length) {
            return origin;
        }

        return origin + (new String(new char[min_length - origin.length()]).replace("\0", padding));

    }

    /**
     * @param str 字符串
     * @return 获取字符串的长度
     */
    public static int length(String str) {

        return str == null ? 0 : str.length();
    }

    /**
     * @param str       字符串
     * @param chunkSize 切割子字符串的长度
     * @return 将字符串每隔  chunkSize 位 插入一个空格
     */
    public static String split(String str, int chunkSize) {

        if (length(str) < chunkSize) {
            return str;
        }
        return String.join(" ", str.split(String.format("(?<=\\G.{%d})", chunkSize)));

    }

    public static String join(String delimiter, Iterable<?> elements) {


        StringJoiner joiner = new StringJoiner(Lino.of(delimiter).get(" "));
        Lira.of(elements).getRaw().stream().map(String::valueOf).forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * @param delimiter 分割符
     * @param elements  数组
     * @return 将数组转换为字符串并通过 delimiter 连接起来
     */
    public static String join(String delimiter, Object... elements) {

        return join(delimiter, Lino.of(elements).map(Arrays::asList).get());
    }

    public static String localMessageStartWith(Throwable throwable, String prefix) {


        while (throwable != null) {

            Throwable cause = throwable.getCause();
            if (cause == null) {
                break;
            }
            throwable = cause;
        }

        if (throwable != null) {

            if (prefix == null) {
                prefix = "";
            }
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            String finalPrefix = prefix;
            return throwable + " at " + Lira.of(stackTrace).map(StackTraceElement::toString)
                    .filter(s -> s.startsWith(finalPrefix)).limit(5).first().get(stackTrace[0].toString());
        }
        return "";
    }

    public static String localMessageAtLineOfClass(Throwable throwable, Class<?> threwClass) {

        return localMessageStartWith(throwable, threwClass.getName());

    }

    public static String localMessageAtLineOfPackage(Throwable throwable, Package _package) {
        return localMessageStartWith(throwable, _package.getName());
    }


    private static boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        final int strOffset = str.length() - suffix.length();


        return LiCharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithAny(final CharSequence sequence, final CharSequence... searchStrings) {
        if (LiStringUtils.isEmpty(sequence) || LiArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (endsWith(sequence, searchString)) {
                return true;
            }
        }
        return false;
    }


}
