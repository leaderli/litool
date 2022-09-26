package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/17 7:11 PM
 */
class PrimitiveEnumTest {

    @Test
    void read() {

        Assertions.assertThrows(IllegalStateException.class, () -> PrimitiveEnum.INT.read(null));
        Assertions.assertEquals(1, (int) PrimitiveEnum.INT.read(1));
        Assertions.assertEquals(1, (int) PrimitiveEnum.INT.read(1.0));
        Assertions.assertEquals(true, PrimitiveEnum.BOOLEAN.read(1.0));
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
