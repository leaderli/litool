package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 1:22 PM
 */
class ObjectsUtilTest {

    @SuppressWarnings("ConstantValue")
    @Test
    void notEquals() {

        Assertions.assertFalse(ObjectsUtil.notEquals(null, null));
        Assertions.assertTrue(ObjectsUtil.notEquals(1, null));
        Assertions.assertTrue(ObjectsUtil.notEquals(null, 1));
        Assertions.assertFalse(ObjectsUtil.notEquals(1, 1));
        Assertions.assertTrue(ObjectsUtil.notEquals(1, 2));

    }

    @Test
    void requireNotNull() {
        Assertions.assertThrows(NullPointerException.class, () -> ObjectsUtil.requireNotNull(1, null));
        Assertions.assertThrows(NullPointerException.class, () -> ObjectsUtil.requireNotNull("not null", 1, null));

        Assertions.assertTrue(ObjectsUtil.anyNull(null, null));
        Assertions.assertFalse(ObjectsUtil.anyNull(1, 2));
    }

    @Test
    void testHashCode() {

        Assertions.assertEquals(-1, ObjectsUtil.hashCodeOrZero(null));
        Assertions.assertEquals(1, ObjectsUtil.hashCodeOrZero(1));
    }

    @Test
    void sameAny() {

        Assertions.assertTrue(ObjectsUtil.sameAny(1, 1));
        assert ObjectsUtil.sameAny(1, 1, 2);
        assert ObjectsUtil.sameAny(1, 1, null);
        assert ObjectsUtil.sameAny(null, 1, null);
        assert !ObjectsUtil.sameAny(null, 1, 2);
        assert !ObjectsUtil.sameAny(3, 1, 2);
    }


}
