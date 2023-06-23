package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RangeIntTest {

    @Test
    void test() {

        RangeInt rangeInt = new RangeInt(2);

        for (int i = 0; i < 100; i++) {
            assertEquals(i % 3, rangeInt.next());
        }
        rangeInt = new RangeInt(1, 2);

        for (int i = 0; i < 100; i++) {
            assertEquals(i % 2 + 1, rangeInt.next());
        }
        rangeInt = new RangeInt(0, 60);
        assertEquals(0, rangeInt.next());
        assertEquals(1, rangeInt.next());

        rangeInt = new RangeInt(0, 60);
        assertEquals(0, rangeInt.next());
        assertEquals(0, rangeInt.iterator().next());

    }
}
