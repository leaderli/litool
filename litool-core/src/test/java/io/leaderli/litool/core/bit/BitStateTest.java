package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.exception.AssertException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/8/16
 */
class BitStateTest {
    BitState bit = new BitState() {
        @Override
        public String toString() {
            return "Modifier:" + BitStr.of(Modifier.class).beauty(getCurrentState());
        }
    };

    @Test
    void set() {
        bit.setState(123);
        Assertions.assertEquals("Modifier:VOLATILE|SYNCHRONIZED|FINAL|STATIC|PRIVATE|PUBLIC", bit.toString());
        bit.setState(0);
        Assertions.assertEquals("Modifier:", bit.toString());
        Assertions.assertThrows(AssertException.class, () -> bit.setState(-1));

    }

    @Test
    void enable() {
        bit.enable(Modifier.FINAL);
        Assertions.assertTrue(bit.only(Modifier.FINAL));
        Assertions.assertThrows(AssertException.class, () -> bit.enable(-1));
    }

    @Test
    void disable() {
        bit.disable(Modifier.FINAL);
        Assertions.assertTrue(bit.none());
        Assertions.assertThrows(AssertException.class, () -> bit.disable(-1));
    }

    @Test
    void has() {
        bit.setState(123);
        Assertions.assertTrue(bit.has(Modifier.PUBLIC));

        bit.disable(Modifier.FINAL);
        Assertions.assertFalse(bit.has(Modifier.FINAL));

    }

    @Test
    void lacks() {
        Assertions.assertTrue(bit.lacks(Modifier.FINAL));
        bit.setState(Modifier.FINAL | Modifier.PUBLIC);
        Assertions.assertFalse(bit.lacks(Modifier.FINAL));
    }

    @Test
    void only() {
        Assertions.assertFalse(bit.only(Modifier.FINAL));
        bit.setState(Modifier.FINAL | Modifier.PUBLIC);
        Assertions.assertFalse(bit.only(Modifier.FINAL));
        bit.disable(Modifier.PUBLIC);
        Assertions.assertTrue(bit.only(Modifier.FINAL));

    }

    @Test
    void none() {
        Assertions.assertTrue(bit.none());
        bit.setState(1);
        Assertions.assertFalse(bit.none());
    }

    @Test
    void any() {
        Assertions.assertFalse(bit.any());
        bit.setState(1);
        Assertions.assertTrue(bit.any());

    }


}

