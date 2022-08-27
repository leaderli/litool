package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/8/16
 */
class BitPermissionTest {
BitPermission<Modifier> bit = new BitPermission<>(Modifier.class);

@BeforeEach

public void before() {
}


@Test
void set() {
    bit.set(123);
    Assertions.assertEquals("Modifier:VOLATILE|SYNCHRONIZED|FINAL|STATIC|PRIVATE|PUBLIC", bit.toString());
    bit.set(0);
    Assertions.assertEquals("Modifier:", bit.toString());

}

@Test
void enable() {
    bit.enable(Modifier.FINAL);
    Assertions.assertTrue(bit.only(Modifier.FINAL));
}

@Test
void disable() {
    bit.disable(Modifier.FINAL);
    Assertions.assertTrue(bit.none());
}

@Test
void have() {
    bit.set(123);
    Assertions.assertTrue(bit.have(Modifier.PUBLIC));

    bit.disable(Modifier.FINAL);
    Assertions.assertFalse(bit.have(Modifier.FINAL));

}

@Test
void miss() {
    Assertions.assertTrue(bit.miss(Modifier.FINAL));
    bit.set(Modifier.FINAL | Modifier.PUBLIC);
    Assertions.assertFalse(bit.miss(Modifier.FINAL));
}

@Test
void only() {
    Assertions.assertFalse(bit.only(Modifier.FINAL));
    bit.set(Modifier.FINAL | Modifier.PUBLIC);
    Assertions.assertFalse(bit.only(Modifier.FINAL));
    bit.disable(Modifier.PUBLIC);
    Assertions.assertTrue(bit.only(Modifier.FINAL));

}

@Test
void none() {
    Assertions.assertTrue(bit.none());
    bit.set(1);
    Assertions.assertFalse(bit.none());
}

@Test
void any() {
    Assertions.assertFalse(bit.any());
    bit.set(1);
    Assertions.assertTrue(bit.any());

}


}

