package io.leaderli.litool.core.bit;


/**
 * @author leaderli
 * @since 2022/6/14
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

    public String toBinary() {
        return Integer.toBinaryString(value);
    }
}
