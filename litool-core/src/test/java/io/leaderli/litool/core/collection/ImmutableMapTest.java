package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class ImmutableMapTest {

@Test
void of() {


    Assertions.assertTrue(ImmutableMap.of(null).isEmpty());
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    Assertions.assertFalse(ImmutableMap.of(src).isEmpty());

    ImmutableMap<String, String> immutableMap = ImmutableMap.of(src);
    src.put("2", "2");

    Assertions.assertNull(immutableMap.get("2"));

    src.remove("1");
    Assertions.assertEquals("1", immutableMap.get("1"));


    Assertions.assertDoesNotThrow(() -> ImmutableMap.of(null));
}

@Test
void size() {
    Assertions.assertSame(0, ImmutableMap.of(null).size());
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    Assertions.assertSame(1, ImmutableMap.of(src).size());
}

@Test
void isEmpty() {
    Assertions.assertTrue(ImmutableMap.of(null).isEmpty());
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    Assertions.assertFalse(ImmutableMap.of(src).isEmpty());
}

@Test
void containsKey() {
    Assertions.assertFalse(ImmutableMap.of(null).containsKey("1"));
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    Assertions.assertTrue(ImmutableMap.of(src).containsKey("1"));
}

@Test
void containsValue() {
    Assertions.assertFalse(ImmutableMap.of(null).containsValue("1"));
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    Assertions.assertTrue(ImmutableMap.of(src).containsValue("1"));
}

@Test
void get() {
    Assertions.assertNull(ImmutableMap.of(null).get("1"));
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    ImmutableMap<String, String> immutable = ImmutableMap.of(src);
    src.put("2", "2");
    Assertions.assertNotNull(immutable.get("1"));
    Assertions.assertNull(immutable.get("2"));
}

@Test
void keySet() {
    Assertions.assertTrue(ImmutableMap.of(null).keySet().isEmpty());
}

@Test
void values() {
    Assertions.assertTrue(ImmutableMap.of(null).values().isEmpty());
}

@Test
void getOrDefault() {
    Assertions.assertEquals(1, ImmutableMap.of(null).getOrDefault("1", 1));
    Map<String, String> src = new HashMap<>();
    src.put("1", "1");
    ImmutableMap<String, String> immutable = ImmutableMap.of(src);
    src.put("2", "2");
    Assertions.assertEquals("1", immutable.getOrDefault("1", "a"));
    Assertions.assertEquals("b", immutable.getOrDefault("2", "b"));
}

@Test
void toMap() {
    Assertions.assertTrue(ImmutableMap.of(null).toMap().isEmpty());
}
}
