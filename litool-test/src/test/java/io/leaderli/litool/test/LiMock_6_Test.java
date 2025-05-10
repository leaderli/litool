package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;
import org.junit.jupiter.api.Assertions;

/**
 * 测试recorder的回收
 */
class LiMock_6_Test {


    @LiTest
    void testRecordCall() {
        LiMock.record(Error6.class, MethodFilter.name("m1"), (method, args, originReturn) -> Assertions.assertEquals(1, args[0]));
        Error6.m1(1);
    }

    @LiTest
    void testRecordCall2() {
        Error6.m1(2);
    }


    static class Error6 {

        public static int m1(int i) {
            return 1;
        }
    }
}
