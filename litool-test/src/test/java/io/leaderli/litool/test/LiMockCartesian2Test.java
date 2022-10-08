package io.leaderli.litool.test;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

class LiMockCartesian2Test {

    static void init() {
        LiMockCartesian.mock(LiMockCartesian2Test.class);
        LiMockCartesian.when(new LiMockCartesian2Test()::m1, 100, 200);
        LiMockCartesian.when(new LiMockCartesian2Test()::m2, -100, -200);


    }

    private int m1() {

        return 1;
    }

    private int m2() {

        return 1;
    }

    @LiMock("init")
    @LiTest
    void test() {

        print(this.m1(), this.m2());

    }
}
