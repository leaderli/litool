package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Random;
import java.util.function.Supplier;

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
     * 生成随机字符串，字符集为{@link #POSSIBLE_CHAR_NUMBER}
     *
     * @param length 随机字符串的长度
     * @return 返回一个随机字符串
     * @see #POSSIBLE_CHAR_NUMBER
     * @see #randomString(String, int)
     */
    public static String randomString(int length) {
        return randomString(POSSIBLE_CHAR_NUMBER, length);
    }

    /**
     * 生成随机字符串，字符集为给定的baseString
     * <p>
     *
     * @param baseString 随机字符集
     * @param length     随机字符串的长度
     * @return 返回一个随机字符串
     * @see #nextInt(int)
     */
    public static String randomString(String baseString, int length) {
        if (StringUtils.isEmpty(baseString)) {
            return StrPool.EMPTY;
        }

        if (length < 1) {
            length = 1;
        }
        final StringBuilder sb = new StringBuilder(length);
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串，字符集为{@link #POSSIBLE_NUMBERS}
     *
     * @param length 随机字符串的长度
     * @return 返回一个随机纯数字字符串
     * @see #POSSIBLE_NUMBERS
     * @see #randomString(String, int)
     */
    public static String randomNumerString(int length) {
        return randomString(POSSIBLE_NUMBERS, length);
    }

    /**
     * 生成0到max之间的随机整数（包括0，不包括max）
     *
     * @param max 上限
     * @return 随机整数
     */
    public static int nextInt(final int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * 生成自然数的随机整数
     *
     * @return 随机自然数
     * @see #nextInt(int)
     */
    public static int nextInt() {

        return RANDOM.nextInt(Integer.MAX_VALUE);
    }


    /**
     * 生成min到max之间的随机整数（包括min，不包括max）
     *
     * @param max 上限
     * @param min 下限
     * @return 随机整数
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
     * @param hit 概率试验次数
     * @return 返回 true 的概率为 1/hit
     */
    public static boolean probability(int hit) {
        return nextInt(hit) == 0;
    }


    /**
     * 运行一次的概率为 1/hit
     *
     * @param hit      随机次数
     * @param runnable 概率被执行的函数
     */
    public static void randomRun(int hit, Runnable runnable) {

        if (probability(hit)) {

            runnable.run();
        }
    }

    /**
     * 返回一个非空值的概率为 1/hit
     *
     * @param hit      随机次数
     * @param supplier 提供者函数
     * @param <T>      提供者泛型
     * @return 1/hit 概率返回有值，其他情况返回空
     */
    public static <T> Lino<T> randomGet(int hit, Supplier<T> supplier) {

        if (probability(hit)) {
            return Lino.supplier(supplier);
        }
        return Lino.none();
    }
}
