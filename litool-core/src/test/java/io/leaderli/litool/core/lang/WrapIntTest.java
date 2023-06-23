package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Assertions;
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


        wrapInt = new WrapInt(1, 4);

        wrapInt.set(-1);
        Assertions.assertEquals(3, wrapInt.get());
        wrapInt.set(-2);
        Assertions.assertEquals(2, wrapInt.get());
        wrapInt.set(2);
        Assertions.assertEquals(2, wrapInt.get());
        wrapInt.set(6);
        Assertions.assertEquals(2, wrapInt.get());
    }
}
