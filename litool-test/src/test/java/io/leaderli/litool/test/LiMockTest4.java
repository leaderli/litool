package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.type.ModifierUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMockTest4 {


    @Test
    void testRecordStatic() {
        LiBox<Object> box = LiBox.none();
        LiMock.recordStatic(Error4.class, ModifierUtil::isStatic, (obj, args, value) -> {
            Assertions.assertEquals(1, value);
            box.value(1);
            return null;
        });
        Error4.m1();
        Assertions.assertTrue(box.present());

        box.reset();
        LiMock.mockStatic(Error4.class, ModifierUtil::isStatic, (method, args) -> 100);
        Error4.m1();
        LiMock.recordStatic(Error4.class, ModifierUtil::isStatic, (obj, args, value) -> {
            Assertions.assertEquals(100, value);
            box.value(1);
            return null;
        });
        Error4.m1();
        Assertions.assertTrue(box.present());
        box.reset();
        LiMock.recordStatic(Error4.class, ModifierUtil::isStatic, (obj, args, value) -> {
            Assertions.assertNull(obj);
            box.value(1);
            return null;
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
