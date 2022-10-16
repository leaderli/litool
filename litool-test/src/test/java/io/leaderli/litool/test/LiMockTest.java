package io.leaderli.litool.test;

import io.leaderli.litool.core.test.IntValues;

class LiMockTest {

    static void init() {
        LiMock.mock(LiMockTest.class);
        LiMock.disable(() -> new LiMockTest().m1());
//        LiMockCartesian.when(new LiMockCartesianTest()::m2, -100, null);
        LiMock.whenArgs(() -> new LiMockTest().m2(0), params -> {
            int len = (int) params[0];
            if (len == 0) {
                return new Object[]{-100, 0};
            }
            return new Object[]{17, 19};
        });

    }

    int m2(int length) {
        return 2;
    }

    void m1() {
        System.out.println("m1");
    }

    @MockInit
    @LiTest
    void test(@IntValues({0, 1}) int length) {

        LiTestAssert.record(Foo.class);
        m1();
        this.m2(1);
        new Foo().init(length, length);


        LiTestAssert.assertReturn("Foo#init", length * 2);
        LiTestAssert.assertArgs("Foo#init", length, length);
        LiTestAssert.assertCalled("Foo#init");
        LiTestAssert.assertNotCalled("Foo#notCall");

    }

    class Foo {
        public int init(int a, int b) {
            return a + b;
        }

        public int notCall(int a, int b) {
            return a + b;
        }
    }
}
