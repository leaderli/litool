package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;

class LiMock_5_Test {


    @LiTest
    void testRecordCall() {
        LiMock.recorder(Error5.class).when(Error5.m1()).called().build();
        Assertions.assertThrows(Throwable.class, () -> new LiMockAfterEachCallback().afterEach(null));
        Assertions.assertDoesNotThrow(() -> new LiMockAfterEachCallback().afterEach(null));
    }

    static class Error5 {


        public static int m1() {
            return 1;
        }
    }
}
