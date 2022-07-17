package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.util.Random;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class LiRandomUtil {

    private static final Random RANDOM = new Random();


    /**
     * @return 随机自然数
     */
    public static int nextInt() {

        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    /**
     * @param min 下边界
     * @param max 上边界
     * @return 返回 min ~ max ,包括 min
     */
    public static int nextInt(int min, int max) {

        LiAssertUtil.assertTrue(max - min >= 0, "Start value must be smaller or equal to end value");
        LiAssertUtil.assertTrue(min >= 0, " Both range values must be non-negative.");

        if (max == min) {
            return min;
        }

        return min + nextInt(max - min);
    }

    /**
     * @param max 上边界
     * @return 返回 0 ~ max，包括 0
     */
    public static int nextInt(final int max) {
        return RANDOM.nextInt(max);
    }

}
