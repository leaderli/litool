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

/**
 * 用于随机选的数字
 */
public static final String BASE_NUMBER = "0123456789";
/**
 * 用于随机选的字符
 */
public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
/**
 * 用于随机选的字符和数字
 */
public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;
private static final Random RANDOM = new Random();

/**
 * 获得一个随机的字符串
 *
 * @param length 字符串的长度
 * @return 随机字符串
 * @see #BASE_CHAR_NUMBER
 * @see #randomString(String, int)
 */
public static String randomString(int length) {
    return randomString(BASE_CHAR_NUMBER, length);
}

/**
 * 获得一个随机的字符串
 *
 * @param baseString 随机字符选取的样本
 * @param length     字符串的长度
 * @return 随机字符串
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
 * @param max 上边界
 * @return 返回 0 ~ max，包括 0
 */
public static int nextInt(final int max) {
    return RANDOM.nextInt(max);
}

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

}
