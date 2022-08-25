package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/8/16
 */
class BitPermissionTest {

    @Test
    void test() {

        BitPermission<Modifier> bit = new BitPermission<>(Modifier.class);

        Assertions.assertTrue(bit.none());

        bit.enable(Modifier.FINAL);
        Assertions.assertTrue(bit.only(Modifier.FINAL));
        bit.disable(Modifier.FINAL);
        Assertions.assertTrue(bit.none());
        Assertions.assertEquals("Modifier:", bit.toString());
        bit.init(123);

        Assertions.assertEquals("Modifier:PUBLIC|PRIVATE|STATIC|FINAL|SYNCHRONIZED|VOLATILE", bit.toString());
        Assertions.assertTrue(bit.any());
        Assertions.assertFalse(bit.only(Modifier.FINAL));
        Assertions.assertTrue(bit.have(Modifier.PUBLIC));

        bit.disable(Modifier.FINAL);
        Assertions.assertTrue(bit.miss(Modifier.FINAL));

    }

    @Test
    void init() {
    }

    @Test
    void enable() {
    }

    @Test
    void disable() {
    }

    @Test
    void have() {
    }

    @Test
    void miss() {
    }

    @Test
    void only() {
    }

    @Test
    void none() {
    }

    @Test
    void any() {
    }
}
