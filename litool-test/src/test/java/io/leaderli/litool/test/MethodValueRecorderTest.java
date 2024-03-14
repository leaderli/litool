package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodValueRecorderTest {

    @Test
    void testAdjustReturnValue() {

        Assertions.assertEquals(0, MethodValueRecorder.adjustReturnValue(null, null, int.class).get());
        Assertions.assertEquals(0, MethodValueRecorder.adjustReturnValue(null, null, Integer.class).get());
        Assertions.assertEquals(Either.none(), MethodValueRecorder.adjustReturnValue("", null, int.class));
        Assertions.assertEquals(1, MethodValueRecorder.adjustReturnValue(1, null, int.class).get());
        Assertions.assertEquals(1, MethodValueRecorder.adjustReturnValue(1, null, Integer.class).get());
        Assertions.assertEquals(0, MethodValueRecorder.adjustReturnValue(Either.right(null), null, Integer.class).get());
        Assertions.assertEquals(Either.none(), MethodValueRecorder.adjustReturnValue(Either.left(null), null, Integer.class));
    }
}
