package io.leaderli.litool.core.bit;


import io.leaderli.litool.core.util.LiStr;

/**
 * @author leaderli
 * @since 2022/6/14
 * <p>
 * 用于使用int来表示位状态时使用，每个枚举值代表着指定位置上的状态是否匹配，
 * 当状态值  state & BX == BX,表示状态BX 匹配
 */

public enum BitStatusEnum {

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


    final int value;
    final int length;

    BitStatusEnum() {
        this.length = Integer.parseInt(this.name().replace("B", "")) - 1;
        this.value = 1 << length;
    }

    @Override
    public String toString() {
        return LiStr.ljust(Integer.toBinaryString(value), 32);
    }



    /**
     * 该枚举值代表的状态位匹配
     */
    public boolean match(int status) {
        return (status & this.value) == this.value;
    }
}
