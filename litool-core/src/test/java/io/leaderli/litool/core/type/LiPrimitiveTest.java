package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/22
 */
class LiPrimitiveTest {

    @Test
    void def() {

        Assertions.assertEquals(0, LiPrimitive.def(int.class));
        Assertions.assertEquals(0, LiPrimitive.def(Integer.class));
        Assertions.assertNull(LiPrimitive.def(String.class));
    }

    @Test
    void toWrapperArray() {
        Assertions.assertSame(Boolean.class, LiPrimitive.toWrapperArray(new boolean[]{}).getClass().getComponentType());
        Assertions.assertSame(Byte.class, LiPrimitive.toWrapperArray(new byte[]{}).getClass().getComponentType());
        Assertions.assertSame(Character.class, LiPrimitive.toWrapperArray(new char[]{}).getClass().getComponentType());
        Assertions.assertSame(Double.class, LiPrimitive.toWrapperArray(new double[]{}).getClass().getComponentType());
        Assertions.assertSame(Float.class, LiPrimitive.toWrapperArray(new float[]{}).getClass().getComponentType());
        Assertions.assertSame(Integer.class, LiPrimitive.toWrapperArray(new int[]{}).getClass().getComponentType());
        Assertions.assertSame(Long.class, LiPrimitive.toWrapperArray(new long[]{}).getClass().getComponentType());
        Assertions.assertSame(Short.class, LiPrimitive.toWrapperArray(new short[]{}).getClass().getComponentType());
    }


}
