package io.leaderli.litool.core.test;

import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/8/19
 */
class CartesianUtilTest {

    @Test
    void test() {

        Assertions.assertSame(Integer.class, CartesianUtil.cartesian(int.class)[0].getClass());
        Assertions.assertNull(CartesianUtil.cartesian(String.class)[0]);


        Field field = ReflectUtil.getField(TestA.class, "age").get();

        Assertions.assertEquals("[1, 2]", Arrays.toString(CartesianUtil.cartesian(field)));
        field = ReflectUtil.getField(TestA.class, "height").get();
        Assertions.assertEquals("[0]", Arrays.toString(CartesianUtil.cartesian(field)));
    }


    private static class TestA {

        @IntValues({1, 2})
        @DoubleValues({1, 2})
        private int age;
        private int height;

    }

}
