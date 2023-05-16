package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举类型，用 int 表示状态，每个枚举值 BX 表示在二进制中的第 X 位
 * 例如：
 *
 * <pre>
 *     0001 B1  // 1
 *     0010 B2  // 2
 *     0100 B3  // 4
 * </pre>
 *
 * @since 2022/6/14
 */
public enum BitPositionEnum {

    B1(1), // 0001 B1  // 1
    B2(1 << 1), // 0010 B2  // 2
    B3(1 << 2), // 0100 B3  // 4
    B4(1 << 3), // 1000 B4  // 8
    B5(1 << 4), // 0001 0000 B5  // 16
    B6(1 << 5), // 0010 0000 B6  // 32
    B7(1 << 6), // 0100 0000 B7  // 64
    B8(1 << 7), // 1000 0000 B8  // 128
    B9(1 << 8), // 0001 0000 0000 B9  // 256
    B10(1 << 9), // 0010 0000 0000 B10  // 512
    B11(1 << 10), // 0100 0000 0000 B11  // 1024
    B12(1 << 11), // 1000 0000 0000 B12  // 2048
    B13(1 << 12), // 0001 0000 0000 0000 B13  // 4096
    B14(1 << 13), // 0010 0000 0000 0000 B14  // 8192
    B15(1 << 14), // 0100 0000 0000 0000 B15  // 16384
    B16(1 << 15), // 1000 0000 0000 0000 B16  // 32768
    B17(1 << 16), // 0001 0000 0000 0000 0000 B17  // 65536
    B18(1 << 17), // 0010 0000 0000 0000 0000 B18  // 131072
    B19(1 << 18), // 0100 0000 0000 0000 0000 B19  // 262144
    B20(1 << 19), // 1000 0000 0000 0000 0000 B20  // 524288
    B21(1 << 20), // 0001 0000 0000 0000 0000 0000 B21  // 1048576
    B22(1 << 21), // 0010 0000 0000 0000 0000 0000 B22  // 2097152
    B23(1 << 22), // 0100 0000 0000 0000 0000 0000 B23  // 4194304
    B24(1 << 23), // 1000 0000 0000 0000 0000 0000 B24  // 8388608
    B25(1 << 24), // 0001 0000 0000 0000 0000 0000 0000 B25  // 16777216
    B26(1 << 25), // 0010 0000 0000 0000 0000 0000 0000 B26  // 33554432
    B27(1 << 26), // 0100 0000 0000 0000 0000 0000 0000 B27  // 67108864
    B28(1 << 27), // 1000 0000 0000 0000 0000 0000 0000 B28  // 134217728
    B29(1 << 28), // 0001 0000 0000 0000 0000 0000 0000 0000 B29  // 268435456
    B30(1 << 29), // 0010 0000 0000 0000 0000 0000 0000 0000 B30  // 536870912
    B31(1 << 30), // 0100 0000 0000 0000 0000 0000 0000 0000 B31  // 1073741824
    B32(1 << 31); // 1000 0000 0000 0000 0000 0000 0000 0000 B32  // -2147483648

    /**
     * 用于表示状态的整数值，每个枚举值对应二进制中的一位
     */
    final int value;

    BitPositionEnum(int value) {
        this.value = value;
    }

    /**
     * 获取二进制状态和枚举值的映射关系
     *
     * @return 二进制状态和枚举值的映射关系
     */
    public static Map<Integer, BitPositionEnum> getBitStatusMap() {
        return Arrays.stream(values()).collect(Collectors.toMap(bit -> bit.value, bit -> bit));
    }

    /**
     * 根据指定的状态值，获取状态中存在的枚举值的迭代器
     *
     * @param status 状态值，用 int 表示
     * @return 存在于状态中的枚举值的 {@link  Iterator}
     */
    public static Iterator<BitPositionEnum> of(int status) {
        BitPositionEnum[] values = values();
        ArrayUtils.reverse(values);

        return Arrays.stream(values).filter(b -> (b.value & status) == b.value).iterator();
    }

    /**
     * 将枚举值对应的二进制位数转换成二进制字符串，并以每 4 位分割
     *
     * @return 以 4 位分割的二进制字符串
     */
    @Override
    public String toString() {
        return StringUtils.chunk(StringUtils.ljust(Integer.toBinaryString(value), 32, '0'), 4);
    }

}
