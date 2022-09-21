package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/13 2:46 PM
 */
class BeanPathTest {


    @ParameterizedTest
    @ValueSource(strings = {"", " ", ".", "key1.", "key1..a", "[", "]", "[]", "a[", "a]", "a[]", "a[a]", "a[1-1]"})
    void valid_build(String expression) {
        Assertions.assertThrows(IllegalStateException.class, () -> BeanPath.parse(null, expression));

    }

    @ParameterizedTest
    @ValueSource(strings = {"key", "key1.key2", "key1[0]", "key1[0].key2", "key1[0][0]", "[0]", "[0][0]", "[0].key", "[-1]"})
    void illegal_build(String expression) {
        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, expression));
    }

    @Test
    void build() {


        Assertions.assertThrows(BeanPath.BeginIllegalStateException.class, () -> BeanPath.parse(null, "."));
        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.parse(null, "["));
        Assertions.assertThrows(BeanPath.BeginIllegalStateException.class, () -> BeanPath.parse(null, "]"));

        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.parse(null, "abc.."));
        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.parse(null, "abc.["));
        Assertions.assertThrows(BeanPath.KeyEndIllegalStateException.class, () -> BeanPath.parse(null, "abc.]"));

        Assertions.assertThrows(BeanPath.ArrayEndIllegalStateException.class, () -> BeanPath.parse(null, "a[1]]"));
        Assertions.assertThrows(BeanPath.ArrayEndIllegalStateException.class, () -> BeanPath.parse(null, "a[1]a"));

        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.parse(null, "a."));
        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.parse(null, "a["));
        Assertions.assertThrows(BeanPath.NotCompleteIllegalStateException.class, () -> BeanPath.parse(null, "a[1"));

        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, "a"));
        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, "a.1"));
        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, "a[1]"));
        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, "a[1][1]"));
        Assertions.assertDoesNotThrow(() -> BeanPath.parse(null, "a[1].b"));


    }


    @Test
    void parse() {

        Map<String, Object> origin = new HashMap<>();
        origin.put("123", "123");
        origin.put("list", Arrays.asList(1, 2, 3));
        origin.put("list2", Arrays.asList(Arrays.asList(1, 2), Arrays.asList(11, 12, 13, 14.0, 15.0)));
        origin.put("map", new HashMap<>(origin));
        origin.put("list3", Arrays.asList(new HashMap<>(origin), new HashMap<>(origin)));
        origin.put("list4", new int[]{1, 2, 3});
        Assertions.assertEquals("123", BeanPath.parse(origin, "123").get());
        Assertions.assertEquals("[1, 2, 3]", BeanPath.parse(origin, "list").get().toString());
        Assertions.assertEquals(3, BeanPath.parse(origin, "list[2]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list[3]"));
        Assertions.assertEquals(12, BeanPath.parse(origin, "list2[1][1]").get());
        Assertions.assertEquals(12, BeanPath.parse(origin, "list2[-1][-4]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list2[1][5]"));
        Assertions.assertEquals(Lino.none(), BeanPath.parse(origin, "list2[3][5]"));

        Assertions.assertEquals(3, BeanPath.parse(origin, "list4[2]").get());

        Assertions.assertEquals("123", BeanPath.parse(origin, "map.123").get());
        Assertions.assertEquals(12,
                BeanPath.parse(origin, "map.list2[1][1]").get());
        Assertions.assertEquals(Lino.none(), BeanPath.parse(null, "map.list2[1][1" +
                "]"));
        Assertions.assertEquals(Lino.none(), BeanPath.parse(null, "map[0]"));

        Assertions.assertEquals(12, BeanPath.parse(origin, "list2[0][1]", l -> l.toLira().size() > 2).get());
        Assertions.assertEquals(15.0, BeanPath.parse(origin, "list2[0][1]", l -> l.toLira().size() > 2,
                l -> l.cast(Double.class)).get());


    }


    private static class Inner {

        private final String name = RandomUtil.randomString(4);
        private int age;


    }
}
