package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Random;

/**
 * @author leaderli
 * @since 2022/6/25
 */
public class RandomUtil {

    public static final String POSSIBLE_NUMBERS = "0123456789";
    public static final String POSSIBLE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String POSSIBLE_CHAR_NUMBER = POSSIBLE_CHAR + POSSIBLE_NUMBERS;
    private static final Random RANDOM = new Random();

    /**
     * a random string, possible char is {@link  #POSSIBLE_CHAR_NUMBER}
     *
     * @param length the length of random string
     * @return a random string
     * @see #POSSIBLE_CHAR_NUMBER
     * @see #randomString(String, int)
     */
    public static String randomString(int length) {
        return randomString(POSSIBLE_CHAR_NUMBER, length);
    }

    /**
     * Return a random string consist of baseString
     *
     * @param baseString A sample of random characters
     * @param length     the length of random string
     * @return a random string
     */
    public static String randomString(String baseString, int length) {
        if (StringUtils.isEmpty(baseString)) {
            return StrPool.EMPTY;
        }
        final StringBuilder sb = new StringBuilder(length);

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    /**
     * Return random number of 0 to max, include 0 exclude max
     *
     * @param max upper boundary
     * @return random number of 0 to max, include 0 exclude max
     */
    public static int nextInt(final int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * @return random natural number
     * @see #nextInt(int)
     */
    public static int nextInt() {

        return RANDOM.nextInt(Integer.MAX_VALUE);
    }


    /**
     * Return random number of min to max, include min exclude max
     *
     * @param max upper boundary
     * @param min lower boundary
     * @return random number of 0 to max, include 0 exclude max
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
     * @param hit probability hit
     * @return 1/hit probability hit
     */
    public static boolean shunt(int hit) {
        return nextInt(hit) == 0;
    }

    /**
     * 随机概率运行一次
     *
     * @param randomInterval 随机值
     * @param runnable       函数
     */
    public static void randomRun(int randomInterval, Runnable runnable) {

        if (nextInt(randomInterval) == 0) {
            runnable.run();
        }
    }

}
