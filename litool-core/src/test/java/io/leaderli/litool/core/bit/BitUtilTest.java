package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author leaderli
 * @since 2022/6/16 11:41 AM
 */
class BitUtilTest {


    @Test
    void onlyOneBit() {
        assertFalse(BitUtil.onlyOneBit(1 >> 2));
        assertTrue(BitUtil.onlyOneBit(1));
        assertTrue(BitUtil.onlyOneBit(1 << 2));
        assertTrue(BitUtil.onlyOneBit(1 << 31));
        assertFalse(BitUtil.onlyOneBit((1 << 2) + 1));

        assertFalse(BitUtil.onlyOneBit(-1));
        assertFalse(BitUtil.onlyOneBit(0));
        assertTrue(BitUtil.onlyOneBit(Integer.MIN_VALUE));
        assertFalse(BitUtil.onlyOneBit(Integer.MAX_VALUE));

        for (int i = 0; i < 100; i++) {
            int num = (int) (Math.random() * Integer.MAX_VALUE);
            if (Integer.bitCount(num) == 1) {
                assertTrue(BitUtil.onlyOneBit(num));
            } else {
                assertFalse(BitUtil.onlyOneBit(num));
            }
        }

    }

    @Test
    void beauty() {

        int bitStatus = Modifier.classModifiers();
        Assertions.assertEquals("0000 0000 0000 0000 0000 1100 0001 1111", BitUtil.beauty(bitStatus));
    }
}
