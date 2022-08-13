package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.UnsupportedClassException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 1:22 PM
 */
class ObjectsUtilTest {

    @Test
    void sameAny() {

        assert ObjectsUtil.sameAny(1, 1);
        assert ObjectsUtil.sameAny(1, 1, 2);
        assert ObjectsUtil.sameAny(1, 1, null);
        assert ObjectsUtil.sameAny(null, 1, null);
        assert !ObjectsUtil.sameAny(null, 1, 2);
        assert !ObjectsUtil.sameAny(3, 1, 2);
    }

    @Test
    void test() {

        System.out.println(Integer.class.isAssignableFrom(Number.class));
        System.out.println(int.class.isAssignableFrom(Number.class));
        System.out.println(Number.class.isAssignableFrom(Integer.class));
        System.out.println(Number.class.isAssignableFrom(int.class));
    }


    @Test
    void compare() {

        Assertions.assertEquals(0, ObjectsUtil.compare(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> ObjectsUtil.compare(1, null));
        Assertions.assertThrows(NullPointerException.class, () -> ObjectsUtil.compare(null, 1));

        Assertions.assertEquals(0, ObjectsUtil.compare(1.0, 1));
        Assertions.assertEquals(-1, ObjectsUtil.compare(1.0, 1.1));
        Assertions.assertEquals(1, ObjectsUtil.compare(1.2, 1.1));

        Assertions.assertEquals(0, ObjectsUtil.compare("1", "1"));
        Assertions.assertEquals(-1, ObjectsUtil.compare("11", "12"));
        Assertions.assertEquals(1, ObjectsUtil.compare("1.2", "1.1"));

        Assertions.assertEquals(0, ObjectsUtil.compare(new int[]{}, new int[]{}));
        Assertions.assertEquals(-1, ObjectsUtil.compare(new int[]{}, new int[]{1}));
        Assertions.assertEquals(1, ObjectsUtil.compare(new int[]{1}, new int[]{}));
        Object a = new Object();
        Object b = new Object();
        Assertions.assertThrows(UnsupportedClassException.class, () -> ObjectsUtil.compare(a, b));

    }
}
