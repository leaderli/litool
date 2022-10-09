package io.leaderli.litool.test;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

class LiMockCartesianTest {

    static void init() {
        LiMockCartesian.mock(LiMockCartesianTest.class);
        LiMockCartesian.when(new LiMockCartesianTest()::m1, 100, 200);
//        LiMockCartesian.when(new LiMockCartesianTest()::m2, -100, null);
        LiMockCartesian.when(new LiMockCartesianTest()::m2);

    }

    int m1() {
        return 1;
    }

    int m2() {
        return 2;
    }

    @LiMock("init")
    @LiTest
    void test() {

        print(this.m1(), this.m2());

    }
}
