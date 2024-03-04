package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.AbstractList;
import java.util.List;

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
    void test() {
        int modifiers = Modifier.STATIC | Modifier.PUBLIC;
        Assertions.assertTrue(Modifier.isPublic(modifiers));
        Assertions.assertTrue(Modifier.isStatic(modifiers));
    }

    @Test
    void isAbstact() {

        Assertions.assertTrue(ModifierUtil.isAbstract(AbstractList.class));
        Assertions.assertTrue(ModifierUtil.isAbstract(List.class));
        Assertions.assertFalse(ModifierUtil.isAbstract(Integer.class));

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
    void priority() throws NoSuchMethodException {


        Assertions.assertEquals(4, ModifierUtil.priority(ModifierUtilTest.class.getMethod("toString")));
        Assertions.assertEquals(2, ModifierUtil.priority(Object.class.getDeclaredMethod("finalize")));
        Assertions.assertEquals(0, ModifierUtil.priority(ModifierUtilTest.class.getDeclaredMethod("m2")));
        Assertions.assertEquals(1, ModifierUtil.priority(ModifierUtilTest.class.getDeclaredMethod("priority")));

    }

    @Test
    void isFinal() throws NoSuchMethodException {

        Assertions.assertTrue(ModifierUtil.isFinal(ModifierUtilTest.class.getMethod("getClass")));
        Assertions.assertFalse(ModifierUtil.isFinal(ModifierUtilTest.class.getDeclaredMethod("isPublic")));

    }

    @Test
    void isProtected() throws NoSuchMethodException {
        Assertions.assertFalse(ModifierUtil.isProtected(ModifierUtilTest.class.getMethod("getClass")));
    }

    @Test
    void isPrivate() throws NoSuchMethodException {
        Assertions.assertFalse(ModifierUtil.isPrivate(ModifierUtilTest.class.getMethod("getClass")));
    }


}
