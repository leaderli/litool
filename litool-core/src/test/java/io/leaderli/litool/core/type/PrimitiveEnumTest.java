package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/17 7:11 PM
 */
class PrimitiveEnumTest {

    @Test
    void  to() {

        System.out.println(PrimitiveEnum.VOID);
    }
    @Test
    void def() {

        Assertions.assertEquals(0, PrimitiveEnum.def(int.class));
        Assertions.assertEquals(0, PrimitiveEnum.def(Integer.class));
        Assertions.assertNull(PrimitiveEnum.def(String.class));
    }

    @Test
    void toWrapperArray() {
        Assertions.assertSame(Boolean.class, PrimitiveEnum.toWrapperArray(new boolean[]{}).getClass().getComponentType());
        Assertions.assertSame(Byte.class, PrimitiveEnum.toWrapperArray(new byte[]{}).getClass().getComponentType());
        Assertions.assertSame(Character.class, PrimitiveEnum.toWrapperArray(new char[]{}).getClass().getComponentType());
        Assertions.assertSame(Double.class, PrimitiveEnum.toWrapperArray(new double[]{}).getClass().getComponentType());
        Assertions.assertSame(Float.class, PrimitiveEnum.toWrapperArray(new float[]{}).getClass().getComponentType());
        Assertions.assertSame(Integer.class, PrimitiveEnum.toWrapperArray(new int[]{}).getClass().getComponentType());
        Assertions.assertSame(Long.class, PrimitiveEnum.toWrapperArray(new long[]{}).getClass().getComponentType());
        Assertions.assertSame(Short.class, PrimitiveEnum.toWrapperArray(new short[]{}).getClass().getComponentType());
        Assertions.assertSame(Object.class, PrimitiveEnum.toWrapperArray(new Object[]{}).getClass().getComponentType());
        Assertions.assertSame(String.class, PrimitiveEnum.toWrapperArray(new String[]{}).getClass().getComponentType());
        Assertions.assertNull(PrimitiveEnum.toWrapperArray(""));
        Assertions.assertNull(PrimitiveEnum.toWrapperArray(null));
        Assertions.assertSame(2,PrimitiveEnum.toWrapperArray(new int[]{1,2}).length);
    }

    @Test
    void test() {

        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(void.class));
        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(Void.class));
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get(null));

    }

}
