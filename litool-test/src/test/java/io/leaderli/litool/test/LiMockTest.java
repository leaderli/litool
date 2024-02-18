package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

class LiTestTest {

    @BeforeEach
    public void before() {
        LiMock.mockStatic(Error.class, (name, argsType, args, resultType) -> {
            if (args.length == 0) {
                return 3;
            }
            return (int) args[0] + 1;
        }, true);
    }

    @LiTest
    public void test() {
        Assertions.assertEquals(3, Error.m1());
        Assertions.assertEquals(5, Error.m1(4));
    }

    static class Error {
        static {
            if (1 == 1) {
                throw new IllegalStateException();
            }
        }

        public static int m1() {
            return 1;
        }

        public static int m1(int a) {
            return a + 1;
        }
    }


}
