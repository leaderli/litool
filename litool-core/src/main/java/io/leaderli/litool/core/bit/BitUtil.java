package io.leaderli.litool.core.bit;

/**
 * @author leaderli
 * @since 2022/6/16 11:37 AM
 */
public class BitUtil {


    /**
     *
     * @param x  数字
     * @return   已二进制表示时，仅包含一个1，包括符号位s
     */
    public static boolean onlyOneBit(int x) {

        if (x < 1 && x > Integer.MIN_VALUE) {
            return false;
        }
        return (x & (x - 1)) == 0;
    }


}
