package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/17
 */
class LiTypeMapTest {

    @Test
    void put() {
        LiTypeMap liTypeMap = new LiTypeMap();

        String value = liTypeMap.get(String.class).get();


        Assertions.assertNull(value);

        liTypeMap.put(String.class, "");
        value = liTypeMap.get(String.class).get();
        Assertions.assertNotNull(value);
    }

    @Test
    void computeIfAbsent() {
        LiTypeMap liTypeMap = new LiTypeMap();

        String value = liTypeMap.computeIfAbsent(String.class, () -> "123").get();
        Assertions.assertEquals("123", value);
    }

    @Test
    void get() {
        LiTypeMap liTypeMap = new LiTypeMap();

        Assertions.assertTrue(liTypeMap.get(String.class).notPresent());
        liTypeMap.put(String.class, "");
        Assertions.assertTrue(liTypeMap.get(String.class).isPresent());


        liTypeMap.put(int.class, 1);
        Assertions.assertEquals(liTypeMap.get(int.class), liTypeMap.get(Integer.class));
        liTypeMap.put(Integer.class, 2);
        Assertions.assertSame(liTypeMap.get(int.class).get(), 2);
    }

    @Test
    void remove() {
        LiTypeMap liTypeMap = new LiTypeMap();

        Assertions.assertTrue(liTypeMap.get(String.class).notPresent());
        String v1 = "";
        liTypeMap.put(String.class, v1);
        liTypeMap.put(CharSequence.class, "");

        Assertions.assertEquals("", liTypeMap.get(String.class).get());
        Assertions.assertEquals("", liTypeMap.get(CharSequence.class).get());

        liTypeMap.remove(String.class);

        Assertions.assertNull(liTypeMap.get(String.class).get());
        Assertions.assertEquals("", liTypeMap.get(CharSequence.class).get());
    }
}
