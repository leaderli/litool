package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnumUtilTest {

    @Test
    void test() {

        Assertions.assertEquals(PrimitiveEnum.BYTE, EnumUtil.of(PrimitiveEnum.class, "BYTE"));

    }

}
