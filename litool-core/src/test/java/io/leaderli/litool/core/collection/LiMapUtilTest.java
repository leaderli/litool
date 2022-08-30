package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings({"rawtypes", "unchecked"})
class LiMapUtilTest {

    @Test
    void override() {

        Assertions.assertTrue(LiMapUtil.override(null, null).isEmpty());
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
    void merge() {

        Assertions.assertTrue(LiMapUtil.merge(null, null).isEmpty());
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
        LiMapUtil.merge(map1, map2);

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


        map1 = new HashMap<>();

        Map map311 = new HashMap();
        map311.put(1, 1);
        map1.put("map", map311);
        map2 = new HashMap<>();
        Map map321 = new HashMap();
        map321.put(1, 4);
        map2.put("map", map321);


    }


    @Test
    void getTypeList() {
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "1");
        List<String> list = new ArrayList<>();
        list.add("1");
        map.put("k2", list);

        assertTrue(LiMapUtil.getTypeList(map, "k1").isEmpty());
        assertEquals(1, LiMapUtil.getTypeList(map, "k2").size());

        assertTrue(LiMapUtil.getTypeList(map, "k1", String.class).isEmpty());
        assertEquals(1, LiMapUtil.getTypeList(map, "k2", String.class).size());
        assertEquals(1, LiMapUtil.getTypeList(map, "k2", String.class).size());
        assertTrue(LiMapUtil.getTypeList(map, "k2", Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(map, "k3", Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(null, "k3", Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(map, null, Integer.class).isEmpty());
        assertTrue(LiMapUtil.getTypeList(map, "k2", null).isEmpty());
    }


    @Test
    void getTypeObject() {
        Map<String, Object> map = new HashMap<>();
        assertFalse(LiMapUtil.getTypeObject(map, "k1", String.class).present());
        assertFalse(LiMapUtil.getTypeObject(map, "k1").present());
        assertFalse(LiMapUtil.getTypeObject(null, "k1").present());
        assertFalse(LiMapUtil.getTypeObject(map, null).present());

        map.put("k1", "k1");
        assertTrue(LiMapUtil.getTypeObject(map, "k1").present());
        assertTrue(LiMapUtil.getTypeObject(map, "k1", String.class).present());
        assertFalse(LiMapUtil.getTypeObject(map, "k1", Integer.class).present());

        map.put("k1", 1);
        assertFalse(LiMapUtil.getTypeObject(map, "k1").present());
        assertFalse(LiMapUtil.getTypeObject(map, "k1", String.class).present());
        assertTrue(LiMapUtil.getTypeObject(map, "k1", Integer.class).present());

        map.put("k1", null);
        assertFalse(LiMapUtil.getTypeObject(map, "k1").present());
        assertFalse(LiMapUtil.getTypeObject(map, "k1", String.class).present());
        assertFalse(LiMapUtil.getTypeObject(map, "k1", Integer.class).present());
    }


    @Test
    void getTypeMap() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> k2 = new HashMap<>();
        k2.put("1", "1");
        k2.put("2", 2);
        map.put("k2", k2);
        assertTrue(LiMapUtil.getTypeMap(map, "k1").isEmpty());
        assertEquals(1, LiMapUtil.getTypeMap(map, "k2").size());
        assertEquals(1, LiMapUtil.getTypeMap(map, "k2", String.class).size());
        assertEquals(1, LiMapUtil.getTypeMap(map, "k2", Integer.class).size());
        assertEquals(2, LiMapUtil.getTypeMap(map, "k2", Object.class).size());
        assertEquals(2, LiMapUtil.getTypeMap(map, "k2", String.class, Object.class).size());
        assertEquals(2, LiMapUtil.getTypeMap(map, "k2", Object.class, Object.class).size());
        assertEquals(2, LiMapUtil.getTypeMap(map, "k2", CharSequence.class, Object.class).size());

        assertEquals(0, LiMapUtil.getTypeMap(map, "k2", null).size());
        assertEquals(0, LiMapUtil.getTypeMap(map, "k2", null, null).size());
        assertEquals(0, LiMapUtil.getTypeMap(map, "k2", null, Object.class).size());
        assertEquals(0, LiMapUtil.getTypeMap(null, "k2").size());
    }


}
