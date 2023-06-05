package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/17 7:11 PM
 */
class PrimitiveEnumTest {
    @Test
    void checkNotPrimitive() {

        Assertions.assertThrows(RuntimeException.class, () -> PrimitiveEnum.checkNotPrimitive(int.class));
        Assertions.assertDoesNotThrow(() -> PrimitiveEnum.checkNotPrimitive(Integer.class));
        Assertions.assertDoesNotThrow(() -> PrimitiveEnum.checkNotPrimitive(null));
        Assertions.assertDoesNotThrow(() -> PrimitiveEnum.checkNotPrimitive(ParameterizedTypeImpl.make(null,
                ArrayList.class, String.class)));
        Assertions.assertDoesNotThrow(() -> PrimitiveEnum.checkNotPrimitive(List.class.getTypeParameters()[0]));
    }

    @Test
    void read() {

        Assertions.assertEquals(0, (int) PrimitiveEnum.INT.read(null));
        Assertions.assertEquals(1, (int) PrimitiveEnum.INT.read(1));
        Assertions.assertEquals(1, (int) PrimitiveEnum.INT.read(1.0));
        Assertions.assertEquals(true, PrimitiveEnum.BOOLEAN.read(1.0));
        Assertions.assertEquals(true, PrimitiveEnum.BOOLEAN.read("true"));
    }

    @Test
    void isNumber() {

        Assertions.assertTrue(PrimitiveEnum.isNumber(1));
        Assertions.assertTrue(PrimitiveEnum.isNumber(1.0));
        Assertions.assertTrue(PrimitiveEnum.isNumber(1.f));
        Assertions.assertFalse(PrimitiveEnum.isNumber((Object) null));
    }

    @Test
    void zero_value() {

        Assertions.assertEquals(0, PrimitiveEnum.zero_value(int.class));
        Assertions.assertEquals(0, PrimitiveEnum.zero_value(Integer.class));
        Assertions.assertNull(PrimitiveEnum.zero_value(String.class));
    }


    @Test
    void get() {

        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(void.class));
        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(Void.class));
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get(null));

    }

}
