package io.leaderli.litool.core.util;

/**
 * @author leaderli
 * @since 2022/6/15
 */
public class LiStr {
    /**
     * @see #ljust(String, int, String)
     */
    public static String ljust(String origin, int min_length) {
        return ljust(origin, min_length, null);
    }


    /**
     * 当位数达不到最小长度时，在左侧补充占位符，默认占位符为空格
     *
     * @param origin     原字符串
     * @param min_length 最小长度
     * @param padding    占位符
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
     * @see #rjust(String, int, String)
     */
    public static String rjust(String origin, int min_length) {
        return rjust(origin, min_length, null);
    }

    /**
     * 当位数达不到最小长度时，在右侧补充占位符，默认占位符为空格
     *
     * @param origin     原字符串
     * @param min_length 最小长度
     * @param padding    占位符
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
}
