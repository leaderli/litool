package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiValue;
import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author leaderli
 * @since 2022/6/16 1:26 PM
 */
class BooleanUtilTest {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void test() {

        Object a;

        a = Lino.of(null);
        Assertions.assertFalse(BooleanUtil.parse(a));
        a = Lino.of(1);
        Assertions.assertTrue(BooleanUtil.parse(a));


        a = false;
        Assertions.assertFalse(BooleanUtil.parse(a));
        a = true;
        Assertions.assertTrue(BooleanUtil.parse(a));

        a = new ArrayList<>();
        Assertions.assertFalse(BooleanUtil.parse(a));
        a = Arrays.asList(1, 2);
        Assertions.assertTrue(BooleanUtil.parse(a));

        a = Collections.emptyIterator();
        Assertions.assertFalse(BooleanUtil.parse(a));
        a = Arrays.asList(1, 2).iterator();
        Assertions.assertTrue(BooleanUtil.parse(a));

        a = new HashMap<>();
        Assertions.assertFalse(BooleanUtil.parse(a));
        ((Map) a).put("1", "1");
        Assertions.assertTrue(BooleanUtil.parse(a));

        a = null;
        Assertions.assertFalse(BooleanUtil.parse(a));
        a = "1";
        Assertions.assertTrue(BooleanUtil.parse(a));


        a = 0;
        Assertions.assertFalse(BooleanUtil.parse(a));


        Assertions.assertDoesNotThrow(() -> BooleanUtil.parse((LiValue) null));

        System.out.println(BooleanUtil.parse(Optional.empty()));
    }

    @Test
    void negate() {
        Assertions.assertTrue(BooleanUtil.negate(false));
        Assertions.assertFalse(BooleanUtil.negate(true));
    }
}
