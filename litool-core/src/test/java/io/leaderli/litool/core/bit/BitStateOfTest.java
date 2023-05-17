package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/9/22 8:53 AM
 */
class BitStateOfTest {

    @Test
    void of() throws NoSuchFieldException {

        BitStateOf of = BitStateOf.the(Modifier.class);
        int modifiers = BitStateOf.class.getDeclaredField("stateClass").getModifiers();
        of.setState(modifiers);
        Assertions.assertEquals("FINAL|PRIVATE", of.toString());
    }

}
