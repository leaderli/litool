package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.util.LiStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/14
 */
class BitEnumTest {

    @Test
    public void test() {

        assert BitStatusEnum.valueOf("B1").value == 1;
        assert BitStatusEnum.valueOf("B32").value == Integer.MIN_VALUE;

        Assertions.assertEquals("0001",LiStr.ljust(BitStatusEnum.B1.toBinary(),4,"0"));
        Assertions.assertEquals("00010000",LiStr.ljust(BitStatusEnum.B5.toBinary(),8,"0"));


    }
}
