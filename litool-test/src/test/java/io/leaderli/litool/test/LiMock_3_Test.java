package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;

class LiMock_3_Test {

    static {
        LiMock.skipClassInitializer(Error3.class);
    }


    @LiTest
    public void testMockStatic() {
        Assertions.assertDoesNotThrow(Error3::m1);
    }


    static class Error3 {
        static {
            if (3 == 3) {
                throw new IllegalStateException("3 == 3");
            }
        }

        public static int m1() {
            return 1;
        }
    }
}
