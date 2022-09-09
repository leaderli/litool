package io.leaderli.litool.core.test;

import com.google.gson.Gson;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/19
 */
class CartesianUtilTest {

    CartesianContext context = new CartesianContext();

    @Test
    void test() {

        CartesianObject<TestA> testACartesian = new CartesianObject<>(TestA.class,
                field -> CartesianUtil.cartesian(field
                        , context));

        Lira<TestA> cartesian = testACartesian.cartesian();


        Assertions.assertEquals(8, cartesian.size());


    }

    @Test
    void cartesian_single_def() {

        Assertions.assertSame(Integer.class, CartesianUtil.cartesian_single_def(int.class)[0].getClass());
        Assertions.assertNull(CartesianUtil.cartesian_single_def(String.class)[0]);
        Assertions.assertThrows(NullPointerException.class, () -> {
            Object o = CartesianUtil.cartesian_single_def(null)[0];
        });
    }

    @Test
    void testField() {


        Field field = ReflectUtil.getField(TestA.class, "age").get();

        Assertions.assertEquals("[1, 2, 1]", Arrays.toString(CartesianUtil.cartesian(field, context)));
        field = ReflectUtil.getField(TestA.class, "height").get();
        Assertions.assertEquals("[180, 173]", Arrays.toString(CartesianUtil.cartesian(field, context)));


        Parameter[] haves = ReflectUtil.getMethod(TestA.class, "have").get().getParameters();
        Assertions.assertEquals("[10, 20]", Arrays.toString(CartesianUtil.cartesian(haves[0], context)));
        Assertions.assertEquals(8, CartesianUtil.cartesian(haves[1], context).length);

        Parameter only = ReflectUtil.getMethod(TestA.class, "only").get().getParameters()[0];


        Assertions.assertEquals("[null]", Arrays.toString(CartesianUtil.cartesian(only, context)));
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void cartesianByTemplate() {

        //language=JSON
        String json = "{\n" +
                "  \"height\": " +
                "188,\n" +
                "  \"gender\": [\n" +
                "    true,\n" +
                "    false\n" +
                "  ],\n" +
                "  \"age\": [\n" +
                "    1,\n" +
                "    2\n" +
                "  ]\n" +
                "}";

        Gson gson = new Gson();
        Map map = gson.fromJson(json, Map.class);


        int size = CartesianUtil.cartesianByTemplate(TestA.class, map).size();
        Assertions.assertEquals(4, size);

    }

    private static class TestA {

        @IntValues({1, 2, 1})
        private int age;
        @IntValues({180, 173})
        private int height;

        @BooleanValues
        private boolean gender;


        public void have(@IntValues({10, 20}) int age, @ObjectValues TestA testA) {

        }

        public void only(TestA testA) {

        }
    }

}
