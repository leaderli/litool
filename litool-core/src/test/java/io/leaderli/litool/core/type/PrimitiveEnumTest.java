package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
    void getByName() {

        Assertions.assertEquals(PrimitiveEnum.INT, PrimitiveEnum.get("int"));
        Assertions.assertEquals(PrimitiveEnum.OBJECT, PrimitiveEnum.get((String) null));

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

        Assertions.assertTrue(PrimitiveEnum.isNumber(int.class));
        Assertions.assertTrue(PrimitiveEnum.isNumber(Integer.class));

        Assertions.assertFalse(PrimitiveEnum.isNumber(char.class));

        Assertions.assertThrows(IllegalStateException.class, () -> PrimitiveEnum.CHAR.read("abc"));
    }

    @Test
    void zero_value() {

        Assertions.assertEquals(0, PrimitiveEnum.zero_value(int.class));
        Assertions.assertEquals(0, PrimitiveEnum.zero_value(Integer.class));
        Assertions.assertNull(PrimitiveEnum.zero_value(String.class));
    }


    @Test
    void get() throws NoSuchMethodException {

        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(void.class));
        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(Void.class));
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get((Class<?>) null));
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get(Supplier.class.getGenericSuperclass()));
        LiTypeToken<Supplier<Integer>> type = new LiTypeToken<Supplier<Integer>>() {
        };
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get(type.getRawType()));
        Type get = TypeUtil.resolve(type, Supplier.class.getMethod("get").getGenericReturnType());
        Assertions.assertSame(PrimitiveEnum.INT, PrimitiveEnum.get(get));

        for (PrimitiveEnum value : PrimitiveEnum.values()) {
            Assertions.assertSame(value, PrimitiveEnum.get(value.wrapper));
            value.read(1);
            value.read(null);
            if (PrimitiveEnum.isNumber(value)) {

                Assertions.assertThrows(IllegalStateException.class, () -> value.read("abc"));
            }
        }
    }

}
