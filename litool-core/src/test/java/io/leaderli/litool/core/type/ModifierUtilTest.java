package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/9
 */
class ModifierUtilTest {

    static void m1() {

    }

    @Test
    void isPublic() throws NoSuchMethodException {

        Assertions.assertFalse(ModifierUtil.isPublic(ModifierUtilTest.class.getDeclaredMethod("isPublic")));
        Assertions.assertTrue(ModifierUtil.isPublic(ModifierUtilTest.class.getMethod("getClass")));
    }

    @Test
    void isStatic() throws NoSuchMethodException {
        Assertions.assertFalse(ModifierUtil.isStatic(ModifierUtilTest.class.getDeclaredMethod("isPublic")));
        Assertions.assertTrue(ModifierUtil.isStatic(ModifierUtilTest.class.getDeclaredMethod("m1")));
    }
}
