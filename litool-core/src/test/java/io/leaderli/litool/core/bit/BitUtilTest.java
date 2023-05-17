package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/6/16 11:41 AM
 */
class BitUtilTest {


    @Test
    void onlyOneBit() {
        assert !BitUtil.onlyOneBit(1 >> 1);
        assert !BitUtil.onlyOneBit(1 >> 2);

        Assertions.assertTrue(BitUtil.onlyOneBit(1));
        assert BitUtil.onlyOneBit(1 << 2);
        assert BitUtil.onlyOneBit(1 << 31);

        assert !BitUtil.onlyOneBit((1 << 2) + 1);
    }

    @Test
    void beauty() {

        int bitStatus = Modifier.classModifiers();
        Assertions.assertEquals("0000 0000 0000 0000 0000 1100 0001 1111", BitUtil.beauty(bitStatus));
    }
}
