package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/17
 */
class TypeMapTest {


    @Test
    void put() {
        TypeMap typeMap = new TypeMap();

        String value = typeMap.get(String.class);


        Assertions.assertNull(value);

        typeMap.put(String.class, "");
        value = typeMap.get(String.class);
        Assertions.assertNotNull(value);

        typeMap.put(Integer.class, 1);
        typeMap.put(int.class, 2);

        Assertions.assertEquals(2, typeMap.get(Integer.class));
        Assertions.assertEquals(2, typeMap.get(int.class));
    }

    @Test
    void computeIfAbsent() {
        TypeMap typeMap = new TypeMap();

        String value = typeMap.computeIfAbsent(String.class, () -> "123");
        Assertions.assertEquals("123", value);
    }

    @Test
    void get() {
        TypeMap typeMap = new TypeMap();

        Assertions.assertNull(typeMap.get(String.class));
        typeMap.put(String.class, "");
        Assertions.assertNotNull(typeMap.get(String.class));


        typeMap.put(int.class, 1);
        Assertions.assertEquals(typeMap.get(int.class), typeMap.get(Integer.class));
        typeMap.put(Integer.class, 2);
        Assertions.assertSame(2, typeMap.get(int.class));
    }

    @Test
    void remove() {
        TypeMap typeMap = new TypeMap();

        Assertions.assertNull(typeMap.get(String.class));
        String v1 = "";
        typeMap.put(String.class, v1);
        typeMap.put(CharSequence.class, "");

        Assertions.assertEquals("", typeMap.get(String.class));
        Assertions.assertEquals("", typeMap.get(CharSequence.class));

        typeMap.remove(String.class);

        Assertions.assertNull(typeMap.get(String.class));
        Assertions.assertEquals("", typeMap.get(CharSequence.class));


        typeMap.put(int.class, 1);
        typeMap.remove(int.class);
        Assertions.assertNull(typeMap.get(int.class));

    }
}
