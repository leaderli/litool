package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/9/22 8:53 AM
 */
class PrettyBitPermissionTest {

    @Test
    void of() throws NoSuchFieldException, NoSuchMethodException {

        PrettyBitPermission of = PrettyBitPermission.of(Modifier.class);
        int modifiers = Object.class.getMethod("getClass").getModifiers();
        of.setState(modifiers);
        assertEquals("NATIVE|FINAL|PUBLIC", of.toString());
        assertEquals("NATIVE|FINAL|PUBLIC", of.toString(modifiers));
    }


    @Test
    void setState() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class);
        permission.setState(1);
        assertEquals(1, permission.getCurrentState());

        permission.setState(3);
        assertEquals(3, permission.getCurrentState());

        assertThrows(IllegalArgumentException.class, () -> permission.setState(-1));
    }

    @Test
    void enable() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class);
        permission.enable(1);
        assertEquals(1, permission.getCurrentState());

        permission.enable(2);
        assertEquals(3, permission.getCurrentState());

        assertThrows(IllegalArgumentException.class, () -> permission.enable(-1));
    }

    @Test
    void disable() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class, (3));
        permission.disable(1);
        assertEquals(2, permission.getCurrentState());

        permission.disable(2);
        assertEquals(0, permission.getCurrentState());

        assertThrows(IllegalArgumentException.class, () -> permission.disable(-1));
    }

    @Test
    void has() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class, 3);
        assertTrue(permission.has(1));
        assertTrue(permission.has(2));
        assertFalse(permission.has(4));
        assertFalse(permission.has(-1));
    }

    @Test
    void lacks() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class, (3));
        assertFalse(permission.lacks(1));
        assertFalse(permission.lacks(2));
        assertTrue(permission.lacks(4));
        assertTrue(permission.lacks(-1));
    }

    @Test
    void only() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class, (1));
        assertTrue(permission.only(1));
        assertFalse(permission.only(2));
        assertFalse(permission.only(-1));
    }

    @Test
    void none() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class);
        assertTrue(permission.none());

        permission.setState(1);
        assertFalse(permission.none());
    }

    @Test
    void any() {
        BitPermission permission = PrettyBitPermission.of(Modifier.class);
        assertFalse(permission.any());

        permission.setState(1);
        assertTrue(permission.any());
    }
}
