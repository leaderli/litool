package io.leaderli.litool.core.bit;

/**
 * binary tool
 *
 * @author leaderli
 * @since 2022/6/16 11:37 AM
 */
public class BitUtil {


/**
 * Return The binary expression of int has only single '1'
 *
 * @param binary a int value
 * @return The binary expression of int has only single '1'
 */
public static boolean onlyOneBit(int binary) {

    if (binary < 1 && binary > Integer.MIN_VALUE) {
        return false;
    }
    return (binary & (binary - 1)) == 0;
}


}
