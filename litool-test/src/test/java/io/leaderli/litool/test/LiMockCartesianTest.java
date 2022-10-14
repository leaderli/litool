package io.leaderli.litool.test;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

class LiMockCartesianTest {

    static void init() {
        LiMockCartesian.mock(LiMockCartesianTest.class);
        LiMockCartesian.when(() -> new LiMockCartesianTest().m1());
//        LiMockCartesian.when(new LiMockCartesianTest()::m2, -100, null);
        LiMockCartesian.when(new LiMockCartesianTest()::m2, 3, 4);

    }

    void m1() {
        System.out.println("m1");
    }

    int m2() {
        return 2;
    }

    @LiMock("init")
    @LiTest
    void test() {

        m1();
        print(this.m2());

    }
}
