package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WrapIntTest {

    @Test
    void test() {

        WrapInt wrapInt = new WrapInt(2);

        for (int i = 0; i < 100; i++) {
            assertEquals(i % 3, wrapInt.next());
        }
        wrapInt = new WrapInt(1, 2);

        for (int i = 0; i < 100; i++) {
            assertEquals(i % 2 + 1, wrapInt.next());
        }
        wrapInt = new WrapInt(0, 60);
        assertEquals(0, wrapInt.next());
        assertEquals(1, wrapInt.next());

        wrapInt = new WrapInt(0, 60);
        assertEquals(0, wrapInt.next());
        assertEquals(0, wrapInt.iterator().next());

    }
}
