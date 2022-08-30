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

    private static void m2() {

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

    @Test
    void test() throws NoSuchMethodException {


        Assertions.assertEquals(4, ModifierUtil.priority(ModifierUtilTest.class.getMethod("toString")));
        Assertions.assertEquals(2, ModifierUtil.priority(Object.class.getDeclaredMethod("finalize")));
        Assertions.assertEquals(0, ModifierUtil.priority(ModifierUtilTest.class.getDeclaredMethod("m2")));
        Assertions.assertEquals(1, ModifierUtil.priority(ModifierUtilTest.class.getDeclaredMethod("test")));

    }
}
