package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/7/21
 */
class BitPositionEnumTest {


    @Test
    void newStatusBitPositionEnumMap() {
        Map<Integer, BitPositionEnum> map = BitPositionEnum.newStatusBitPositionEnumMap();
        assertEquals(32, map.size());
        assertEquals(BitPositionEnum.B1, map.get(1));
        assertEquals(BitPositionEnum.B2, map.get(2));
        assertEquals(BitPositionEnum.B3, map.get(4));

        Set<Integer> integers = BitPositionEnum.newStatusBitPositionEnumMap().keySet();
        assert integers.size() == 32;

        Assertions.assertSame(32, Lira.of(integers).filter(BitUtil::onlyOneBit).size());
    }

    @Test
    void of() {
        Assertions.assertSame(BitPositionEnum.B1, BitPositionEnum.of(1).next());
        Iterator<BitPositionEnum> of = BitPositionEnum.of(3);
        Assertions.assertSame(BitPositionEnum.B2, of.next());
        Assertions.assertSame(BitPositionEnum.B1, of.next());

        Iterator<BitPositionEnum> iterator = BitPositionEnum.of(1);
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B1, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = BitPositionEnum.of(3);
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B1, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = BitPositionEnum.of(15);
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B4, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B3, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(BitPositionEnum.B1, iterator.next());
        assertFalse(iterator.hasNext());


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
