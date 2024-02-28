package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;

class LiMock_2_Test {

    static {
        LiMock.skipClassInitializer(Error2.class);
    }

    @LiTest
    public void testMockStatic() {
        LiMock.mockStatic(Error2.class, m -> true, (method, args) -> 100);
        Assertions.assertEquals(100, Error2.m1());
    }


    static class Error2 {
        static {
            if (2 == 2) {
                throw new IllegalStateException("2 == 2");
            }
        }

        public static int m1() {
            return 1;
        }
    }
}
