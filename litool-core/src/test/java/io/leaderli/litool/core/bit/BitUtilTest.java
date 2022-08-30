package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 11:41 AM
 */
class BitUtilTest {

    @Test
    void isPositivePower2() {

        assert !BitUtil.onlyOneBit(1 >> 1);
        assert !BitUtil.onlyOneBit(1 >> 2);

        Assertions.assertTrue(BitUtil.onlyOneBit(1));
        assert BitUtil.onlyOneBit(1 << 2);
        assert BitUtil.onlyOneBit(1 << 31);

        assert !BitUtil.onlyOneBit((1 << 2) + 1);

    }
}
