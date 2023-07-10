package io.leaderli.litool.core.lang;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.LeanKey;
import io.leaderli.litool.core.lang.lean.LeanValue;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author leaderli
 * @since 2022/9/24 9:41 AM
 */
@SuppressWarnings("rawtypes")
class LeanTest {


    Gson gson = new Gson();


    @SuppressWarnings("unchecked")
    @Test
    void test13() {

        String json = "{\"name\":\"1\",\"bean\": {\"name\": \"2\"},\"beans\": [{\"name\": \"3\"}]}";
        Bean1 bean1 = gson.fromJson(json, Bean1.class);

        Lean lean = new Lean();

        Map map = lean.fromBean(bean1, Map.class);
        Assertions.assertTrue(map.get("bean") instanceof Map);
        Assertions.assertTrue(map.get("beans") instanceof List);
        List list = (List) map.get("beans");
        Assertions.assertTrue(list.get(0) instanceof HashMap);

        map.clear();
        map.put(1, 1);
        map.put(true, true);
        map.put("str", "str");

        Map<String, String> strMap = lean.fromBean(map, new LiTypeToken<Map<String, String>>() {
        });

        Assertions.assertEquals("{str=str, 1=1, true=true}", strMap.toString());
        strMap = lean.fromBean(null, new LiTypeToken<Map<String, String>>() {
        });
        Assertions.assertNotNull(strMap);
    }


    @Test
    void test3() {
        String json = "{\"name\":\"1\",\"bean2\": {\"name\": \"2\",\"bean3\": {\"name\": \"3\"}}}";
        Map map = gson.fromJson(json, Map.class);

        Lean lean = new Lean();
        Bean3 parser = lean.fromBean(map, Bean3.class);
        System.out.println(gson.toJson(parser));
//        Assertions.assertEquals("3", parser.beans.get(0).name);

    }

    @Test
    void test4() {
        String json = "{\"name\":\"1\",\"bean\": {\"name\": \"2\"},\"beans\": [{\"name\": \"3\"}]}";
        Map map = gson.fromJson(json, Map.class);
        LiTypeToken<Bean4<Bean4>> parameterized = new LiTypeToken<Bean4<Bean4>>() {
        };

        Lean lean = new Lean();
        Bean4<Bean4> parser = lean.fromBean(map, parameterized);
        Assertions.assertEquals("3", parser.beans.get(0).name);

    }

    @Test
    void test5() {
        String json = "{\"bean1\": {\"name\": \"2\"},\"bean2\": {\"name\": \"3\"}}";
        Map map = gson.fromJson(json, Map.class);

        Lean lean = new Lean();
        Bean5 parser = lean.fromBean(map, Bean5.class);

        Assertions.assertSame(lean.getTypeAdapter(Bean1.class), lean.getTypeAdapter(Bean1.class));

    }

    @Test
    void tes6() {
        String json = "{\"age\": 1.0}";
        Map map = gson.fromJson(json, Map.class);

        Lean lean = new Lean();
        Bean6 parser = lean.fromBean(map, Bean6.class);

        Assertions.assertEquals(1.0, parser.age);


    }

    @Test
    void test7() {
        String json = "{\"age\": 1.0,\"age2\": 2.0,\"age4\":\"4\",\"age3\": 5}";
        Map map = gson.fromJson(json, Map.class);

        Lean lean = new Lean(new LinkedHashMap<>(), Collections.singletonList(f -> {
            if (f.getName().equals("custom")) {
                return "age";
            }
            return null;
        }));
        Bean7 bean7 = lean.fromBean(map, Bean7.class);

        Assertions.assertEquals(1.0, bean7.fake);
        Assertions.assertEquals(2.0, bean7.fake2);
        Assertions.assertEquals(1.0, bean7.custom);
        Assertions.assertEquals(10086.0, bean7.custom2);
        Assertions.assertEquals(4.0, bean7.age4);

    }

    @Test
    void test8() {
        String json = "{\"name\": [\"123\"],\"age\": 10}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Assertions.assertThrows(IllegalArgumentException.class, () -> lean.fromBean(map, Bean8.class));

    }

    @Test
    void test9() {
        String json = "{\"name\": [\"123\"],\"age\": 10}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Bean9 bean = lean.fromBean(map, Bean9.class);
        Assertions.assertEquals("123", bean.name);

    }

    @Test
    void test10() {
        String json = "{\"name\": \"123\"}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Bean10 bean = lean.fromBean(map, Bean10.class);
        Assertions.assertEquals("123", bean.name);
    }

    @Test
    void test11() {
        String json = "{\"name\": [\"123\"],\"ages\": [10,18]}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Bean11<Integer> bean = lean.fromBean(map, new LiTypeToken<Bean11<Integer>>() {
        });
        Assertions.assertArrayEquals(new String[]{"123"}, bean.name);
        Assertions.assertArrayEquals(new Integer[]{10, 18}, bean.ages);
    }


    @Test
    void copyBean() {
        String json = "{\"name\": [\"123\"],\"ages\": [10,18]}";

        Lean lean = new Lean();
        Bean12<Integer> bean = lean.fromBean(gson.fromJson(json, Map.class), new LiTypeToken<Bean12<Integer>>() {
        });
        Assertions.assertEquals(10, bean.ages[0]);
        Assertions.assertThrows(ClassCastException.class, () -> {
                    Bean12<Integer> bean12 = lean.fromBean(gson.fromJson(json, Map.class), new LiTypeToken<Bean12<Integer>>() {
                    }.getGenericType());

                    Integer age = bean12.ages[0];
                }
        );

    }

    @Test
    void test12() {
        String json = "{\"name\": [\"123\"],\"ages\": [10,18]}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Bean11<Integer> bean = lean.fromBean(map, new LiTypeToken<Bean11<Integer>>() {
        });

        Bean12<Integer> copy = new Bean12<>();
        lean.copyBean(bean, copy, new LiTypeToken<Bean12<Integer>>() {
        });

        Assertions.assertArrayEquals(new String[]{"123"}, copy.name);
        Assertions.assertArrayEquals(new Integer[]{10, 18}, copy.ages);
    }

    @Test
    void test133() {
        Bean13 bean13 = new Bean13();
        bean13.size = 13;
        bean13.name = "_13";

        Lean lean = new Lean();
        Bean13 bean131 = new Bean13();
        lean.copyBean(bean13, bean131, Bean13.class);


    }

    @Test
    void test14() {

        Bean14 bean = new Bean14();
        bean.name = 10;
        Bean14.size = 100;

        Lean lean = new Lean();

        Map map = lean.fromBean(bean, Map.class);

        Assertions.assertNull(map.get("size"));

    }

    @Test
    void test15() {

        Bean15 bean15 = new Bean15();
        String json = "{\"name\": \"123\",\"ages\": [10,18]}";
        Map map = gson.fromJson(json, Map.class);
        Lean lean = new Lean();
        Bean15 bean = lean.fromBean(map, new LiTypeToken<Bean15>() {
        });
        Assertions.assertEquals("{\"name\":\"123\"}", gson.toJson(bean));
    }


    private static class StringTypeAdapter implements TypeAdapter<String> {

        @Override
        public String read(Object source, Lean lean) {
            if (source instanceof List) {
                return (String) ((List<?>) source).get(0);
            }
            return String.valueOf(source);
        }
    }

    private static class DoubleTypeAdapter implements TypeAdapter<Double> {

        @Override
        public Double read(Lean lean) {
            return (double) 10086;
        }


        @Override
        public Double read(Object source, Lean lean) {
            return (Double) lean.getTypeAdapter(Double.class).read(source, lean);
        }
    }

    private static class ObjectTypeAdapter implements TypeAdapter<List> {

        @Override
        public List read(Object source, Lean lean) {
            return null;
        }
    }

    private static class Bean14 {
        private static int size;
        private int name;
    }

    private static class Bean13 {
        private int size;
        private String name = "13";
    }

    private static class Bean12<T> {
        private String[] name;
        private T[] ages;
    }

    private static class Bean11<T> {
        private String[] name;
        private T[] ages;
    }

    private static class Bean10 {
        private String name;
    }

    private static class Bean9<T extends List> {
        @LeanValue(StringTypeAdapter.class)
        private String name;
        @LeanValue(ObjectTypeAdapter.class)
        private T age;
    }

    private static class Bean8 {
        @LeanValue(StringTypeAdapter.class)
        private String name;
        @LeanValue(StringTypeAdapter.class)
        private int age;
    }

    private static class Bean7 {
        @LeanKey("age")
        private double fake;
        @LeanKey("age2")
        private double fake2;
        private double custom;

        @LeanValue(DoubleTypeAdapter.class)
        private double custom2;

        @LeanValue(DoubleTypeAdapter.class)
        private double age4;
    }

    private static class Bean6 {
        private double age;
    }

    private static class Bean5 {
        private Bean1 bean1;
        private Bean1 bean2;

    }

    @SuppressWarnings("all")
    private static class Bean4<T> {
        private String name;
        private Map bean;
        private List<T> beans;
    }

    private static class Bean3 {
        private String name;
        private Bean2 bean2;
    }

    private static class Bean2 {
        private Bean3 bean3;
        private String name;

    }

    @SuppressWarnings("all")
    private static class Bean1<T> {
        private String name;
        private Bean1 bean;
        private List<T> beans;
    }

    private static class Bean15 extends Bean2 {

    }


}

