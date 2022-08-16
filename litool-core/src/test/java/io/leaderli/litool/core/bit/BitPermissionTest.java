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

        Assertions.assertEquals("Modifier:PUBLIC|PRIVATE|STATIC|PARAMETER_MODIFIERS|SYNCHRONIZED|BRIDGE", bit.toString());
        Assertions.assertTrue(bit.any());
        Assertions.assertFalse(bit.only(Modifier.FINAL));
        Assertions.assertTrue(bit.have(Modifier.PUBLIC));
//        System.out.println(Integer.toBinaryString(123));
//        System.out.println(Integer.toBinaryString(Modifier.FINAL));
//        System.out.println(123 & Modifier.FINAL);
        Assertions.assertTrue(bit.miss(Modifier.FINAL));

    }
}
