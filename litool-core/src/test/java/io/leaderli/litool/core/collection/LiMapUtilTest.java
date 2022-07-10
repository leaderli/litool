package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class LiMapUtilTest {

    @Test
    public void override() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("1", "1");
        map1.put("2", "1");
        List<String> value = new ArrayList<>();
        value.add("l1");
        value.add("l2");
        map1.put("l", value);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("1", "10");
        map2.put("3", "3");

        assertEquals("{1=1, 2=1, l=[l1, l2]}", map1.toString());
        Map<String, Object> override = LiMapUtil.override(map1, map2);
        assertEquals("{1=10, 2=1, l=[l1, l2]}", override.toString());
        Assertions.assertNotEquals(map1, override);

        map1.put("1", 1);
        LiMapUtil.override(map1, map2);

        map1 = new HashMap<>();
        Map<String, String> map11 = new HashMap<>();
        map11.put("1-1", "11");
        map11.put("1-2", "11");
        map1.put("m", map11);
        map2 = new HashMap<>();
        Map<String, String> map12 = new HashMap<>();
        map12.put("1-1", "22");
        map12.put("2-2", "22");
        map2.put("m", map12);


        assertEquals("{m={1-1=22, 1-2=11}}", LiMapUtil.override(map1, map2).toString());


    }


    @Test
    public void merge() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("1", "1");
        map1.put("2", "1");
        List<String> value = new ArrayList<>();
        value.add("l1");
        value.add("l2");
        map1.put("l", value);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("1", "10");
        map2.put("3", "3");

        assertEquals("{1=1, 2=1, l=[l1, l2]}", map1.toString());
        Map<String, Object> override = LiMapUtil.merge(map1, map2);
        assertEquals("{1=10, 2=1, 3=3, l=[l1, l2]}", override.toString());

        Assertions.assertNotEquals(map1, override);

        map1.put("1", 1);
        LiMapUtil.override(map1, map2);

        map1 = new HashMap<>();
        Map<String, String> map11 = new HashMap<>();
        map11.put("1-1", "11");
        map11.put("1-2", "11");
        map1.put("m", map11);
        map2 = new HashMap<>();
        Map<String, String> map12 = new HashMap<>();
        map12.put("1-1", "22");
        map12.put("2-2", "22");
        map2.put("m", map12);


        assertEquals("{m={1-1=22, 1-2=11, 2-2=22}}", LiMapUtil.merge(map1, map2).toString());

    }


    @Test
    public void getTypeList() {
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "1");
        List<String> list = new ArrayList<>();
        list.add("1");
        map.put("k2", list);

        assertTrue(LiMapUtil.getTypeList(map, "k1", String.class).isEmpty());
        assertEquals(1, LiMapUtil.getTypeList(map, "k2", String.class).size());
        assertEquals(1, LiMapUtil.getTypeList(map, "k2", String.class).size());
        assertTrue(LiMapUtil.getTypeList(map, "k2", Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(map, "k3", Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(null, "k3", Integer.class).isEmpty());
    }


    @Test
    public void getTypeObject() {
        Map<String, Object> map = new HashMap<>();
        assertFalse(LiMapUtil.getTypeObject(map, "k1", String.class).present());
        Map<String, String> map2 = new HashMap<>();
        assertFalse(LiMapUtil.getTypeObject(map2, "k1").present());

        assertFalse(LiMapUtil.getTypeObject(null, "k1").present());
    }

    @Test
    public void getTypeMap1() {

        Map<String, Object> map = new HashMap<>();
        assertTrue(LiMapUtil.getTypeMap(map, "k1").isEmpty());
    }

    @Test
    public void getTypeMap2() {

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> k1 = new HashMap<>();
        map.put("str_int", k1);
        k1.put("int", 1);
        assertTrue(LiMapUtil.getTypeMap(map, "str_int").isEmpty());
    }

    @Test
    public void getTypeMap3() {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> k2 = new HashMap<>();
        map.put("str_str", k2);
        k2.put("a", "a");
        assertEquals("a", LiMapUtil.getTypeMap(map, "str_str").get("a"));
        assertEquals("a", LiMapUtil.getTypeMap(map, "str_str", String.class).get("a"));
    }


    @Test
    public void deepGet() {

        String[] keys = null;
        assertSame(Lino.none(), LiMapUtil.deepGet(null, keys));
        assertSame(Lino.none(), LiMapUtil.deepGet(1, keys));

        Map<String, String> map = new HashMap<>();
        assertSame(Lino.none(), LiMapUtil.deepGet(map, keys));
        assertSame(Lino.none(), LiMapUtil.deepGet(map, "a"));

        map.put("a", "1");

        assertEquals(Lino.of("1"), LiMapUtil.deepGet(map, "a"));
        assertNotEquals(Lino.of("1"), LiMapUtil.deepGet(map, "b"));

        Map<String, Object> big = new HashMap<>();
        big.put("map", map);
        assertEquals(Lino.of("1"), LiMapUtil.deepGet(big, "map", "a"));
        assertEquals(Lino.none(), LiMapUtil.deepGet(big, "map2", "a"));

        assertTrue(LiMapUtil.deepGet(big, "map").get() instanceof Map);

    }
}
