package io.leaderli.litool.core.bit;

import com.sun.istack.internal.NotNull;
import io.leaderli.litool.core.text.StringUtils;

import java.util.Arrays;
import java.util.Iterator;

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

    NONE,
    /**
     * 0000 0000 0000 0000 0000 0000 0000 0001 B1
     * 1
     */
    B1,
    /**
     * 0000 0000 0000 0000 0000 0000 0000 0010 B2
     * 2
     */
    B2,
    /**
     * 0000 0000 0000 0000 0000 0000 0000 0100 B3
     * 4
     */
    B3,
    /**
     * 0000 0000 0000 0000 0000 0000 0000 1000 B4
     * 8
     */
    B4,
    /**
     * 0000 0000 0000 0000 0000 0000 0001 0000 B5
     * 16
     */
    B5,
    /**
     * 0000 0000 0000 0000 0000 0000 0010 0000 B6
     * 32
     */
    B6,
    /**
     * 0000 0000 0000 0000 0000 0000 0100 0000 B7
     * 64
     */
    B7,
    /**
     * 0000 0000 0000 0000 0000 0000 1000 0000 B8
     * 128
     */
    B8,
    /**
     * 0000 0000 0000 0000 0000 0001 0000 0000 B9
     * 256
     */
    B9,
    /**
     * 0000 0000 0000 0000 0000 0010 0000 0000 B10
     * 512
     */
    B10,
    /**
     * 0000 0000 0000 0000 0000 0100 0000 0000 B11
     * 1024
     */
    B11,
    /**
     * 0000 0000 0000 0000 0000 1000 0000 0000 B12
     * 2048
     */
    B12,
    /**
     * 0000 0000 0000 0000 0001 0000 0000 0000 B13
     * 4096
     */
    B13,
    /**
     * 0000 0000 0000 0000 0010 0000 0000 0000 B14
     * 8192
     */
    B14,
    /**
     * 0000 0000 0000 0000 0100 0000 0000 0000 B15
     * 16384
     */
    B15,
    /**
     * 0000 0000 0000 0000 1000 0000 0000 0000 B16
     * 32768
     */
    B16,
    /**
     * 0000 0000 0000 0001 0000 0000 0000 0000 B17
     * 65536
     */
    B17,
    /**
     * 0000 0000 0000 0010 0000 0000 0000 0000 B18
     * 131072
     */
    B18,
    /**
     * 0000 0000 0000 0100 0000 0000 0000 0000 B19
     * 262144
     */
    B19,
    /**
     * 0000 0000 0000 1000 0000 0000 0000 0000 B20
     * 524288
     */
    B20,
    /**
     * 0000 0000 0001 0000 0000 0000 0000 0000 B21
     * 1048576
     */
    B21,
    /**
     * 0000 0000 0010 0000 0000 0000 0000 0000 B22
     * 2097152
     */
    B22,
    /**
     * 0000 0000 0100 0000 0000 0000 0000 0000 B23
     * 4194304
     */
    B23,
    /**
     * 0000 0000 1000 0000 0000 0000 0000 0000 B24
     * 8388608
     */
    B24,
    /**
     * 0000 0001 0000 0000 0000 0000 0000 0000 B25
     * 16777216
     */
    B25,
    /**
     * 0000 0010 0000 0000 0000 0000 0000 0000 B26
     * 33554432
     */
    B26,
    /**
     * 0000 0100 0000 0000 0000 0000 0000 0000 B27
     * 67108864
     */
    B27,
    /**
     * 0000 1000 0000 0000 0000 0000 0000 0000 B28
     * 134217728
     */
    B28,
    /**
     * 0001 0000 0000 0000 0000 0000 0000 0000 B29
     * 268435456
     */
    B29,
    /**
     * 0010 0000 0000 0000 0000 0000 0000 0000 B30
     * 536870912
     */
    B30,
    /**
     * 0100 0000 0000 0000 0000 0000 0000 0000 B31
     * 1073741824
     */
    B31,

    /**
     * 1000 0000 0000 0000 0000 0000 0000 0000 B32
     * 2147483648
     */
    B32;


    /**
     * Most Significant Bit，即最高位的比特位。 一个 int 类型的值是表示它的二进制形式是首位为1，其他为0
     */
    public final int mask_msb;
    /**
     * 比特位的位置，为0表示非msb，同时也是其数组形式的角标
     */
    public final int mask_position;

    BitPositionEnum() {
        if ("NONE".equals(this.name())) {
            mask_msb = 0;
            mask_position = 0;
        } else {
            this.mask_position = Integer.parseInt(this.name().substring(1));
            this.mask_msb = 1 << (mask_position - 1);
        }
    }


    /**
     * 根据指定的状态值，获取状态中存在的枚举值的迭代器
     *
     * @param status 状态值，用 int 表示
     * @return 存在于状态中的枚举值的 {@link  Iterator}
     */
    public static BitPositionEnum[] ofs(int status) {

        BitPositionEnum[] bitPositionEnums = values();
        BitPositionEnum[] result = new BitPositionEnum[bitPositionEnums.length];
        int len = 0;
        for (int i = 1; i < bitPositionEnums.length; i++) {
            BitPositionEnum b = bitPositionEnums[i];
            if ((b.mask_msb & status) == b.mask_msb) {
                result[len++] = b;
            }
        }
        return Arrays.copyOf(result, len);
    }

    /**
     * @param x 状态值，用 int 表示
     * @return 存在于状态中的枚举值，如果不存在则返回NONE
     */
    @NotNull
    public static BitPositionEnum of(int x) {
        if (BitUtil.onlyOneBit(x)) {
            int n = 0;
            if ((x >> 16) != 0) {
                n = n + 16;
                x = x >> 16;
            }
            if ((x >> 8) != 0) {
                n = n + 8;
                x = x >> 8;
            }
            if ((x >> 4) != 0) {
                n = n + 4;
                x = x >> 4;
            }
            if ((x >> 2) != 0) {
                n = n + 2;
                x = x >> 2;
            }
            if ((x >> 1) != 0) {
                n = n + 2;
            } else {
                n = n + 1;
            }
            return values()[n];

        }
        return NONE;
    }

    public static BitPositionEnum getByPosition(int position) {
        if (position > 0 && position < 33) {
            return values()[position];
        }
        return NONE;
    }

    /**
     * 将枚举值对应的二进制位数转换成二进制字符串，并以每4位填充空格
     * <p>
     * eg:
     * 0000 1000 0000 0000 0000 0000 0000 0000 B28
     *
     * @return 以 4 位分割的二进制字符串
     */
    @Override
    public String toString() {
        return StringUtils.chunk(StringUtils.ljust(Integer.toBinaryString(mask_msb), 32, '0'), 4);
    }

}
