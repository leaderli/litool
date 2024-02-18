package io.leaderli.litool.test;

import org.junit.jupiter.api.BeforeEach;

class LiTestTest {

    @BeforeEach
    public void before() {
        LiMock.mockStatic(Error.class, MockMethodInvoker::invoke, true);
        System.out.println("before----->");
    }

    @LiTest
    public void test() {

        System.out.println(Error.m1());
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

    }


}
