package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ModifierUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMockTest4 {


    @Test
    void testRecordStatic() {
        LiMock.recordStatic(Error4.class, ModifierUtil::isStatic, (args, value) -> {
            Assertions.assertEquals(1, value);
            return null;
        });
        Error4.m1();
        LiMock.mockStatic(Error4.class, ModifierUtil::isStatic, (method, args) -> 100);
        Error4.m1();
        LiMock.recordStatic(Error4.class, ModifierUtil::isStatic, (args, value) -> {
            Assertions.assertEquals(100, value);
            return null;
        });
        Error4.m1();
    }

    static class Error4 {


        public static int m1() {
            return 1;
        }
    }
}
