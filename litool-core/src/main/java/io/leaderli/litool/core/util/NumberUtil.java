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
}
