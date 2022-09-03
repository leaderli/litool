package io.leaderli.litool.core.bit;


import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * use int to represent status, each enum BX represent the position X on binary position
 * eg:
 * <pre>
 *     0001 B1
 *     0010 B2
 *     0100 B3
 * </pre>
 *
 * @author leaderli
 * @since 2022/6/14
 */

public enum BitPositionEnum {

    B1,
    B2,
    B3,
    B4,
    B5,
    B6,
    B7,
    B8,
    B9,
    B10,
    B11,
    B12,
    B13,
    B14,
    B15,
    B16,
    B17,
    B18,
    B19,
    B20,
    B21,
    B22,
    B23,
    B24,
    B25,
    B26,
    B27,
    B28,
    B29,
    B30,
    B31,
    B32;


    /**
     * When using binary representation value , binary only have single '1'
     */
    final int value;

    BitPositionEnum() {
        int index = Integer.parseInt(this.name().replace("B", "")) - 1;
        this.value = 1 << index;
    }

    public static Map<Integer, BitPositionEnum> getBitStatusMap() {
        return Arrays.stream(values()).collect(Collectors.toMap(bit -> bit.value, bit -> bit));
    }

    /**
     * Return the {@link  BitPositionEnum} that exists on the status binary
     *
     * @param status a int value represent status
     * @return a lira of exist BX
     */
    public static Iterator<BitPositionEnum> of(int status) {

        BitPositionEnum[] values = values();
        ArrayUtils.reverse(values);
        return Arrays.stream(values).filter(b -> (b.value & status) == b.value).iterator();
    }

    @Override
    public String toString() {
        return StringUtils.split(StringUtils.ljust(Integer.toBinaryString(value), 32, "0"), 4);
    }
}
