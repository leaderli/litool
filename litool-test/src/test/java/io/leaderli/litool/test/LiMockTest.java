package io.leaderli.litool.test;

import io.leaderli.litool.core.test.IntValues;

class LiMockTest {

    static void init() {
        LiMock.mock(LiMockTest.class);
        LiMock.light(() -> new LiMockTest().m1());
        LiMock.light(() -> new LiMockTest().m3());
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

    Foo m3() {

        return null;
    }

    @MockInit
    @LiTest
    void test(@IntValues({0, 1}) int length) {

        Foo foo = LiTestAssert.recording(Foo.class);
        m1();
        this.m2(1);
        Foo foo1 = m3();
        foo1.init(length, length);

        LiTestAssert.assertReturn(() -> foo.init(1, 1), length * 2);
        LiTestAssert.assertArgs(() -> foo.init(1, 1), length, length);
        LiTestAssert.assertCalled(() -> foo.init(1, 1));
        LiTestAssert.assertNotCalled(() -> foo.notCall(1, 1));

        foo1.notCall(0, 0);
        LiTestAssert.assertCalled(() -> foo.notCall(1, 1));

    }

    static class Foo {
        public int init(int a, int b) {
            return a + b;
        }

        public int notCall(int a, int b) {
            return a + b;
        }
    }
}
