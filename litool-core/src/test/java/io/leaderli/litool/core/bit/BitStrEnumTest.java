package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author leaderli
 * @since 2022/7/21
 */
class BitPositionEnumTest {


    @Test
    void of() {
        assertEquals(BitPositionEnum.NONE, BitPositionEnum.values()[0]);
        for (int i = 0; i < 32; i++) {
            assertEquals(BitPositionEnum.valueOf("B" + (i + 1)), BitPositionEnum.of(1 << i));
            assertEquals(BitPositionEnum.values()[i + 1], BitPositionEnum.of(1 << i));
            assertEquals(BitPositionEnum.NONE, BitPositionEnum.of((1 << i) + 5));
        }

        for (int i = 1; i < 32; i++) {
            Assertions.assertNotSame(BitPositionEnum.getByPosition(i), BitPositionEnum.NONE);
        }
        Assertions.assertSame(BitPositionEnum.getByPosition(0), BitPositionEnum.NONE);
        Assertions.assertSame(BitPositionEnum.getByPosition(33), BitPositionEnum.NONE);
        Assertions.assertSame(32, Lira.of(BitPositionEnum.values()).map(b -> b.mask_msb).filter(BitUtil::onlyOneBit).size());
    }

    @Test
    void ofs() {
        Assertions.assertArrayEquals(new BitPositionEnum[]{}, BitPositionEnum.ofs(0));
        Assertions.assertArrayEquals(new BitPositionEnum[]{BitPositionEnum.B1}, BitPositionEnum.ofs(1));
        Assertions.assertArrayEquals(new BitPositionEnum[]{BitPositionEnum.B1}, BitPositionEnum.ofs(1));
        Assertions.assertArrayEquals(new BitPositionEnum[]{BitPositionEnum.B1, BitPositionEnum.B2}, BitPositionEnum.ofs(3));
        Assertions.assertArrayEquals(new BitPositionEnum[]{BitPositionEnum.B1, BitPositionEnum.B2, BitPositionEnum.B3, BitPositionEnum.B4}, BitPositionEnum.ofs(15));
        Assertions.assertArrayEquals(new BitPositionEnum[]{BitPositionEnum.B1, BitPositionEnum.B2, BitPositionEnum.B4}, BitPositionEnum.ofs(11));
        Assertions.assertArrayEquals(Arrays.copyOfRange(BitPositionEnum.values(), 1, 33), BitPositionEnum.ofs(-1));

        assert BitPositionEnum.valueOf("B1").mask_msb == 1;
        assert BitPositionEnum.valueOf("B32").mask_msb == Integer.MIN_VALUE;
    }

    @Test
    void testToString() {
        assertEquals("0000 0000 0000 0000 0000 0000 0000 0001", BitPositionEnum.B1.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0000 0010", BitPositionEnum.B2.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0000 0100", BitPositionEnum.B3.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0000 1000", BitPositionEnum.B4.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0001 0000", BitPositionEnum.B5.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0010 0000", BitPositionEnum.B6.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 0100 0000", BitPositionEnum.B7.toString());
        assertEquals("0000 0000 0000 0000 0000 0000 1000 0000", BitPositionEnum.B8.toString());


        Map<BitPositionEnum, String> status = new EnumMap<>(BitPositionEnum.class);

        status.put(BitPositionEnum.B2, "B2");
        status.put(BitPositionEnum.B1, "B1");

        assertEquals("[0000 0000 0000 0000 0000 0000 0000 0001, 0000 0000 0000 0000 0000 0000 0000 0010]", status.keySet().toString());


        assertEquals("0001", StringUtils.ljust(Integer.toBinaryString(BitPositionEnum.B1.mask_msb), 4, '0'));
        assertEquals("00010000", StringUtils.ljust(Integer.toBinaryString(BitPositionEnum.B5.mask_msb), 8, '0'));
    }
}
