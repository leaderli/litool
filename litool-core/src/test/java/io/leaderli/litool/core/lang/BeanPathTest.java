package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/13 2:46 PM
 */
class BeanPathTest {


    @Test
    public void test() {


        Assertions.assertThrows(BeanPath.BeginIllegalStateException.class, () -> BeanPath.of("."));
        Assertions.assertThrows(BeanPath.BeginIllegalStateException.class, () -> BeanPath.of("["));
        Assertions.assertThrows(BeanPath.BeginIllegalStateException.class, () -> BeanPath.of("]"));

        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.of("abc.."));
        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.of("abc.["));
        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.of("abc.]"));

        Assertions.assertThrows(BeanPath.ArrayEndIllegalStateException.class, () -> BeanPath.of("a[1]]"));
        Assertions.assertThrows(BeanPath.ArrayEndIllegalStateException.class, () -> BeanPath.of("a[1]a"));

        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.of("a."));
        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.of("a["));
        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.of("a[1"));

        Assertions.assertDoesNotThrow(() -> BeanPath.of("a"));
        Assertions.assertDoesNotThrow(() -> BeanPath.of("a.1"));
        Assertions.assertDoesNotThrow(() -> BeanPath.of("a[1]"));
        Assertions.assertDoesNotThrow(() -> BeanPath.of("a[1][1]"));
        Assertions.assertDoesNotThrow(() -> BeanPath.of("a[1].b"));


        Map<String, Object> origin = new HashMap<>();
        origin.put("123", "123");
        origin.put("list", Arrays.asList(1, 2, 3));
        origin.put("list2", Arrays.asList(Arrays.asList(1, 2), Arrays.asList(11, 12, 13)));
        origin.put("map", new HashMap<>(origin));
        origin.put("list3", Arrays.asList(new HashMap<>(origin), new HashMap<>(origin)));
        origin.put("list4", new int[]{1, 2, 3});

        Assertions.assertEquals("123", BeanPath.parse(origin, "123").get());
        Assertions.assertEquals("[1, 2, 3]", BeanPath.parse(origin, "list").get().toString());
        Assertions.assertEquals(3, BeanPath.parse(origin, "list[2]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list[3]"));
        Assertions.assertEquals(12, BeanPath.parse(origin, "list2[1][1]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list2[1][3]"));
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list2[3][3]"));

        Assertions.assertEquals(3, BeanPath.parse(origin, "list4[2]").get());

        Assertions.assertEquals("123", BeanPath.parse(origin, "map.123").get());
        Assertions.assertEquals(12, BeanPath.parse(origin, "map.list2[1][1]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(null, "map.list2[1][1]"));
        Assertions.assertEquals(Lino.none(), BeanPath.parse(null, "map[0]"));

        Assertions.assertEquals(3, BeanPath.parse(origin, "list[0]", lino -> lino.cast(Integer.class).filter(i -> i > 2)).get());
        Assertions.assertEquals(12, BeanPath.parse(origin, "list2[0][1]", lino -> lino.cast(List.class).filter(l -> l.size() > 2)).get());
        Assertions.assertEquals(13, BeanPath.parse(origin, "list2[0][1]", lino -> lino.cast(List.class).filter(l -> l.size() > 2), lino -> lino.cast(Integer.class).filter(i -> i > 11)).get());

        Assertions.assertEquals("123", BeanPath.parse(origin, "list3[0].123").get());
        Assertions.assertEquals(13, BeanPath.parse(origin, "list3[0].list2[0][1]", null, lino -> lino.cast(List.class).filter(l -> l.size() > 2), lino -> lino.cast(Integer.class).filter(i -> i > 11)).get());

    }

}
