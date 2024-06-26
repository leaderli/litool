package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.text.StringUtils;

/**
 * binary tool
 *
 * @author leaderli
 * @since 2022/6/16 11:37 AM
 */
public class BitUtil {


    /**
     * 判断整数的二进制表示是否仅包含一个'1'
     *
     * @param num 一个整数值
     * @return 如果整数的二进制表示仅包含一个'1'，则返回true，否则返回false
     * @see Integer#bitCount(int)
     */
    public static boolean onlyOneBit(int num) {
        return Integer.bitCount(num) == 1;
    }

    /**
     * @param num 一个二进制数值
     * @return 将二进制数从16位分割为两个部分
     */
    public static LiTuple<Integer, Integer> split(int num) {
        return split(num, 16);
    }

    /**
     * @param num      一个二进制数值
     * @param position 分割位置
     * @return 将二进制数从指定位置位分割为两个部分
     * eg:
     * 0000 0000 0000 0000 0000 0000 0000 0011
     * 按照位置1切割
     * 0000 0000 0000 0000 0000 0000 0000 0010
     * 0000 0000 0000 0000 0000 0000 0000 0001
     */
    public static LiTuple<Integer, Integer> split(int num, int position) {
        LiAssertUtil.assertTrue(position <= 31 && position > 0, new IllegalArgumentException(position + " position must bettwen 2 and 31"));
        return LiTuple.of(num & (-1 << position), num & ~(-1 << position));
    }

    /**
     * @param num int数值
     * @return 将int数值转为为32位的二进制字符串，每4位使用空格填充
     */
    public static String beauty(int num) {
        return StringUtils.chunk(StringUtils.ljust(Integer.toBinaryString(num), 32, '0'), 4);
    }

}
