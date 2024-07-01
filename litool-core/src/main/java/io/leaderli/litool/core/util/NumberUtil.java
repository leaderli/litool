package io.leaderli.litool.core.util;

/**
 * @author leaderli
 * @since 2023/6/21 12:41 PM
 */
public class NumberUtil {

    /**
     * 值限制在给定范围内
     *
     * @param value 值
     * @param min   最小值
     * @param max   最大值
     * @return 值限制在给定范围内
     */
    public static int constrain(int value, int min, int max) {
        if (value < min) {
            return min;
        } else return Math.min(value, max);
    }

    /**
     * 值限制在给定范围内
     *
     * @param value 值
     * @param min   最小值
     * @param max   最大值
     * @return 值限制在给定范围内
     */
    public static double constrain(double value, double min, double max) {
        if (value < min) {
            return min;
        } else return Math.min(value, max);
    }

    /**
     * 值限制在给定范围内
     *
     * @param value 值
     * @param def   当值不在范围内时的取值
     * @param min   最小值
     * @param max   最大值
     * @return 值限制在给定范围内
     */
    public static int constrain(int value, int def, int min, int max) {
        if (def < min || def > max) {
            throw new IllegalArgumentException("default value is not in range");
        }
        if (value < min || value > max) {
            return def;
        }
        return value;
    }

    /**
     * 值限制在给定范围内
     *
     * @param value 值
     * @param def   当值不在范围内时的取值
     * @param min   最小值
     * @param max   最大值
     * @return 值限制在给定范围内
     */
    public static double constrain(double value, double def, double min, double max) {
        if (def < min || def > max) {
            throw new IllegalArgumentException("default value is not in range");
        }
        if (value < min || value > max) {
            return def;
        }
        return value;
    }


    /**
     * 将字符串转化为int，支持二进制、八进制、十进制、十六进制，根据字符串前缀区分，
     * eg:
     * 0b1010
     * 07777
     * 7777
     * 0xffff
     */

    public static int parserInt(String number) {
        if (number == null) {
            throw new NumberFormatException("For input string:" + number);
        }
        if (number.length() == 1) {
            return Integer.parseInt(number);
        }
        if (number.startsWith("0x") || number.startsWith("0X")) {
            return Integer.parseInt(number.substring(2), 16);
        }
        if (number.startsWith("0b") || number.startsWith("0B")) {
            return Integer.parseInt(number.substring(2), 2);
        }
        if (number.startsWith("0")) {
            return Integer.parseInt(number.substring(1), 8);
        }
        return Integer.parseInt(number);
    }
}
