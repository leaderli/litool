package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/17 7:11 PM
 */
class PrimitiveEnumTest {


    @Test
    void test() {

        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(void.class));
        Assertions.assertSame(PrimitiveEnum.VOID, PrimitiveEnum.get(Void.class));
        Assertions.assertSame(PrimitiveEnum.OBJECT, PrimitiveEnum.get(null));

    }

}
