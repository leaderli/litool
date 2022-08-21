package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/8/19
 */
class CartesianUtilTest {

    @Test
    void test() {

        CartesianObject<TestA> testACartesian = new CartesianObject<>(TestA.class, null);

        Lira<TestA> cartesian = testACartesian.cartesian();


        Assertions.assertEquals(8, cartesian.size());


    }

    @Test
    void testField() {

        Assertions.assertSame(Integer.class, CartesianUtil.cartesian(int.class)[0].getClass());
        Assertions.assertNull(CartesianUtil.cartesian(String.class)[0]);


        Field field = ReflectUtil.getField(TestA.class, "age").get();

        Assertions.assertEquals("[1, 2, 1]", Arrays.toString(CartesianUtil.cartesian(field, null)));
        field = ReflectUtil.getField(TestA.class, "height").get();
        Assertions.assertEquals("[180, 173]", Arrays.toString(CartesianUtil.cartesian(field, null)));


        Parameter[] haves = ReflectUtil.getMethod(TestA.class, "have").get().getParameters();
        Assertions.assertEquals("[10, 20]", Arrays.toString(CartesianUtil.cartesian(haves[0], null)));
        Assertions.assertEquals(8, CartesianUtil.cartesian(haves[1], null).length);

        Parameter only = ReflectUtil.getMethod(TestA.class, "only").get().getParameters()[0];


        Assertions.assertEquals("[null]", Arrays.toString(CartesianUtil.cartesian(only, null)));
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
