package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class TypeUtilTest {

    @Test
    void isUnknown() {

        Assertions.assertTrue(TypeUtil.isUnknown(Consumer.class.getTypeParameters()[0]));
//        LiPrintUtil.print((Object[]) Consumer.class.getTypeParameters());
//        LiPrintUtil.print((Object[]) Function.class.getTypeParameters());
    }

    @Test
    void testGetClass() {


        Assertions.assertSame(Object.class, TypeUtil.getClass(Object.class));
        Assertions.assertSame(Object[].class, TypeUtil.getClass(Object[].class));

        ParameterizedType genericSuperclass = (ParameterizedType) TestType.class.getGenericInterfaces()[0];
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        Assertions.assertSame(String.class, TypeUtil.getClass(actualTypeArgument));


    }

    @Test
    void testEquals() {


        ParameterizedType left = (ParameterizedType) ArrayList.class.getGenericInterfaces()[0];
        ParameterizedType right = (ParameterizedType) AbstractList.class.getGenericInterfaces()[0];
        Assertions.assertTrue(TypeUtil.equals(left, right));
    }


    private static class TestType implements Consumer<String> {


        @Override
        public void accept(String s) {

        }
    }

}
