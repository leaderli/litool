package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/9/22 8:53 AM
 */
class PrettyBitPermissionTest {

    @Test
    void of() throws NoSuchFieldException {

        PrettyBitPermission of = PrettyBitPermission.of(Modifier.class);
        int modifiers = PrettyBitPermission.class.getDeclaredField("statusClass").getModifiers();
        of.setState(modifiers);
        Assertions.assertEquals("FINAL|PRIVATE", of.toString());
    }

}
