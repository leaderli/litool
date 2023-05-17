package io.leaderli.litool.core.bit;

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
     */
    public static boolean onlyOneBit(int num) {

        if (num < 1 && num > Integer.MIN_VALUE) {
            return false;
        }
        return (num & (num - 1)) == 0;
    }


    /**
     * @param num int数值
     * @return 将int数值转为为32位的二进制字符串，每4位使用空格填充
     */
    public static String beauty(int num) {
        return StringUtils.chunk(StringUtils.ljust(Integer.toBinaryString(num), 32, '0'), 4);
    }

}
