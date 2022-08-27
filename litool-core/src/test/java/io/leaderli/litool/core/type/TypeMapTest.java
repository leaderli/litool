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

    String value = typeMap.get(String.class).get();


    Assertions.assertNull(value);

    typeMap.put(String.class, "");
    value = typeMap.get(String.class).get();
    Assertions.assertNotNull(value);
}

@Test
void computeIfAbsent() {
    TypeMap typeMap = new TypeMap();

    String value = typeMap.computeIfAbsent(String.class, () -> "123").get();
    Assertions.assertEquals("123", value);
}

@Test
void get() {
    TypeMap typeMap = new TypeMap();

    Assertions.assertTrue(typeMap.get(String.class).absent());
    typeMap.put(String.class, "");
    Assertions.assertTrue(typeMap.get(String.class).present());


    typeMap.put(int.class, 1);
    Assertions.assertEquals(typeMap.get(int.class), typeMap.get(Integer.class));
    typeMap.put(Integer.class, 2);
    Assertions.assertSame(2, typeMap.get(int.class).get());
}

@Test
void remove() {
    TypeMap typeMap = new TypeMap();

    Assertions.assertTrue(typeMap.get(String.class).absent());
    String v1 = "";
    typeMap.put(String.class, v1);
    typeMap.put(CharSequence.class, "");

    Assertions.assertEquals("", typeMap.get(String.class).get());
    Assertions.assertEquals("", typeMap.get(CharSequence.class).get());

    typeMap.remove(String.class);

    Assertions.assertNull(typeMap.get(String.class).get());
    Assertions.assertEquals("", typeMap.get(CharSequence.class).get());
}
}
