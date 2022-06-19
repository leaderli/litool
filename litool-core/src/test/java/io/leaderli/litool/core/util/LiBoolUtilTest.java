package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author leaderli
 * @since 2022/6/16 1:26 PM
 */
class LiBoolUtilTest {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void test() {

        Object a;

        a = Lino.of(null);
        Assertions.assertFalse(LiBoolUtil.parse(a));
        a = Lino.of(1);
        Assertions.assertTrue(LiBoolUtil.parse(a));


        a = false;
        Assertions.assertFalse(LiBoolUtil.parse(a));
        a = true;
        Assertions.assertTrue(LiBoolUtil.parse(a));

        a = new ArrayList<>();
        Assertions.assertFalse(LiBoolUtil.parse(a));
        a = Arrays.asList(1, 2);
        Assertions.assertTrue(LiBoolUtil.parse(a));

        a = Collections.emptyIterator();
        Assertions.assertFalse(LiBoolUtil.parse(a));
        a = Arrays.asList(1, 2).iterator();
        Assertions.assertTrue(LiBoolUtil.parse(a));

        a = new HashMap<>();
        Assertions.assertFalse(LiBoolUtil.parse(a));
        ((Map) a).put("1", "1");
        Assertions.assertTrue(LiBoolUtil.parse(a));

        a = null;
        Assertions.assertFalse(LiBoolUtil.parse(a));
        a = "1";
        Assertions.assertTrue(LiBoolUtil.parse(a));

    }
}
