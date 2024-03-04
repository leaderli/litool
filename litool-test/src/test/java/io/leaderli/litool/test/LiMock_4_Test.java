package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.type.MethodFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMock_4_Test {


    @Test
    void testRecordStatic() {
        LiBox<Object> box = LiBox.none();
        LiMock.recordStatic(Error4.class, MethodFilter.isMethod(), (m, args, value) -> {
            Assertions.assertEquals(1, value);
            box.value(1);
        });
        Error4.m1();
        Assertions.assertTrue(box.present());

        box.reset();
        LiMock.mockStatic(Error4.class, MethodFilter.isMethod(), (method, args) -> 100);
        Error4.m1();
        LiMock.recordStatic(Error4.class, MethodFilter.isMethod(), (m, args, value) -> {
            Assertions.assertEquals(100, value);
            box.value(1);
        });
        Error4.m1();
        Assertions.assertTrue(box.present());
        box.reset();
        LiMock.recordStatic(Error4.class, MethodFilter.isMethod(), (m, args, value) -> {
            box.value(1);
        });
        Error4.m1();
        Assertions.assertTrue(box.present());

        box.reset();

    }

    static class Error4 {


        public static int m1() {
            return 1;
        }
    }
}
