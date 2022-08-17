package io.leaderli.litool.core.util;

import com.google.gson.Gson;
import io.leaderli.litool.core.exception.UnsupportedTypeException;
import io.leaderli.litool.core.meta.ra.DefaultSomeRa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

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

        Assertions.assertEquals(0, ObjectsUtil.compare(true, true));
        Assertions.assertEquals(-1, ObjectsUtil.compare(false, true));
        Assertions.assertEquals(1, ObjectsUtil.compare(true, false));

        Assertions.assertEquals(0, ObjectsUtil.compare(new int[]{}, new int[]{}));
        Assertions.assertEquals(-1, ObjectsUtil.compare(new int[]{}, new int[]{1}));
        Assertions.assertEquals(1, ObjectsUtil.compare(new int[]{1}, new int[]{}));

        Object a = new Object();
        Object b = new Object();
        Assertions.assertThrows(UnsupportedTypeException.class, () -> ObjectsUtil.compare(a, b));


    }
}
