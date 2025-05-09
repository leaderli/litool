package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodValueRecorderTest {

    @Test
    void testAdjustReturnValue() {

        Assertions.assertEquals(0, MethodValueFactory.adjustReturnValue(null, null, int.class).get());
        Assertions.assertEquals(0, MethodValueFactory.adjustReturnValue(null, null, Integer.class).get());
        Assertions.assertEquals(Either.none(), MethodValueFactory.adjustReturnValue("", null, int.class));
        Assertions.assertEquals(1, MethodValueFactory.adjustReturnValue(1, null, int.class).get());
        Assertions.assertEquals(1, MethodValueFactory.adjustReturnValue(1, null, Integer.class).get());
        Assertions.assertEquals(0, MethodValueFactory.adjustReturnValue(null, null, Integer.class).get());
        Assertions.assertEquals(Either.none(), MethodValueFactory.adjustReturnValue(Either.left(null), null, Integer.class));
    }
}
