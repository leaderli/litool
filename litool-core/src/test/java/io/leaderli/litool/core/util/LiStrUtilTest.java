package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * @author leaderli
 * @since 2022/6/15
 */
class LiStrUtilTest {

    @Test
    public void test() {

    }

    @Test
    public void ljust() {

        Assertions.assertEquals("***1", LiStrUtil.ljust("1", 4, "*"));
        Assertions.assertEquals("   1", LiStrUtil.ljust("1", 4));
        Assertions.assertEquals("    ", LiStrUtil.ljust(null, 4));
        Assertions.assertEquals("12345", LiStrUtil.ljust("12345", 4));
    }

    @Test
    public void rjust() {

        Assertions.assertEquals("1***", LiStrUtil.rjust("1", 4, "*"));
        Assertions.assertEquals("1   ", LiStrUtil.rjust("1", 4));
        Assertions.assertEquals("    ", LiStrUtil.rjust(null, 4));
        Assertions.assertEquals("12345", LiStrUtil.rjust("12345", 4));
    }

    @Test
    public void split() {

        Assertions.assertNull(LiStrUtil.split(null, 4));
        Assertions.assertEquals("123", LiStrUtil.split("123", 4));
        Assertions.assertEquals("12 3", LiStrUtil.split("123", 2));
        Assertions.assertEquals("12 34", LiStrUtil.split("1234", 2));
        Assertions.assertEquals("12 34 5", LiStrUtil.split("12345", 2));
    }

    @Test
    void join() {

        Assertions.assertEquals("1 2 3", LiStrUtil.join(" ", 1, 2, 3));
        Assertions.assertEquals("", LiStrUtil.join(" ", (Object[]) null));
        Assertions.assertEquals("", LiStrUtil.join(" ", (Object) null));
        Assertions.assertEquals("1 2", LiStrUtil.join(null, 1, 2));
        Assertions.assertEquals("", LiStrUtil.join(null, Collections.singletonList(null)));
        Assertions.assertEquals("1 2", LiStrUtil.join(null, Arrays.asList(1, 2)));
    }


    private void outmock() {
        try {

            mock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mock() {

        try {

            Lino.of(0).map(i -> new Random().nextInt(i));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void line() {

        try {

            outmock();
        } catch (Exception e) {
            System.out.println();
            Assertions.assertTrue(LiStrUtil.localMessageAtLineOfClass(e, Lino.class).contains("Some.map("));
            Assertions.assertTrue(LiStrUtil.localMessageAtLineOfClass(e, LiStrUtilTest.class).contains("mock$0"));
        }
        Lino.of(0).throwable_map(i -> 5 / i, e -> Assertions.assertTrue(LiStrUtil.localMessageAtLineOfClass(e, Lino.class).contains("Some.throwable_map(")));
        Lino.of(0).throwable_map(i -> 5 / i, e -> Assertions.assertTrue(LiStrUtil.localMessageAtLineOfPackage(e, Lino.class.getPackage()).contains("Some.throwable_map(")));
        Lino.of(0).throwable_map(i -> 5 / i, e -> Assertions.assertTrue(LiStrUtil.localMessageAtLineOfPackage(e, LiStrUtil.class.getPackage()).contains("line$")));
    }
}
