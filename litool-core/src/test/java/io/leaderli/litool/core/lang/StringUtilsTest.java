package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/15
 */
class StringUtilsTest {

    @Test
    void just() {
        Assertions.assertEquals("**1**", StringUtils.just("1", 4, '*'));
        Assertions.assertEquals("  1  ", StringUtils.just("1", 4));
        Assertions.assertEquals("    ", StringUtils.just(null, 4));
        Assertions.assertEquals("12345", StringUtils.just("12345", 4));
    }

    @Test
    void ljust() {

        Assertions.assertEquals("***1", StringUtils.ljust("1", 4, '*'));
        Assertions.assertEquals("   1", StringUtils.ljust("1", 4));
        Assertions.assertEquals("    ", StringUtils.ljust(null, 4));
        Assertions.assertEquals("12345", StringUtils.ljust("12345", 4));
    }

    @Test
    void rjust() {

        Assertions.assertEquals("1***", StringUtils.rjust("1", 4, '*'));
        Assertions.assertEquals("1   ", StringUtils.rjust("1", 4));
        Assertions.assertEquals("    ", StringUtils.rjust(null, 4));
        Assertions.assertEquals("12345", StringUtils.rjust("12345", 4));
    }

    @Test
    void split() {

        Assertions.assertNull(StringUtils.chunk(null, 4));
        Assertions.assertEquals("123", StringUtils.chunk("123", 4));
        Assertions.assertEquals("12 3", StringUtils.chunk("123", 2));
        Assertions.assertEquals("12 34", StringUtils.chunk("1234", 2));
        Assertions.assertEquals("12 34 5", StringUtils.chunk("12345", 2));
    }

    @Test
    void join() {

        Assertions.assertEquals("1 2 3", StringUtils.join(" ", 1, 2, 3));
        Assertions.assertEquals("", StringUtils.join(" ", (Object[]) null));
        Assertions.assertEquals("", StringUtils.join(" ", (Object) null));
        Assertions.assertEquals("1,2", StringUtils.join(null, 1, 2));
        Assertions.assertEquals(",1", StringUtils.join(null, Arrays.asList(null, 1)));
        Assertions.assertEquals(",1", StringUtils.join(",", Arrays.asList(null, 1)));
        Assertions.assertEquals("1,2", StringUtils.join(null, Arrays.asList(1, 2)));
        Assertions.assertEquals("1,2", StringUtils.join(null, Stream.of(1, 2)));
    }

    @SuppressWarnings("all")
    @Test
    void obj2String() {

        FieldsString obj = new FieldsString();
        obj.name = "hello";


        Assertions.assertEquals("FieldsString{name=hello, age=0}", StringUtils.obj2String(obj));
        Assertions.assertEquals("1", StringUtils.obj2String(1));
        Assertions.assertEquals("null", StringUtils.obj2String(null));


    }

    @Test
    void line() {

        try {

            outmock();
        } catch (Exception e) {
            Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, Lino.class).contains("Lino$Some.map("));
            Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, StringUtilsTest.class).contains(
                    "StringUtilsTest.lambda"));
            Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, null).length() > 0);
        }
        Lino.of(0).throwable_map(i -> 5 / i, e -> Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e,
                Lino.class).contains("Some.throwable_map(")));
        Lino.of(0).throwable_map(i -> 5 / i, e -> Assertions.assertTrue(StringUtils.localMessageAtLineOfPackage(e,
                Lino.class.getPackage()).contains("Some.throwable_map(")));
        Lino.of(0).throwable_map(i -> 5 / i, e -> {
            String s = StringUtils.localMessageAtLineOfPackage(e, StringUtilsTest.class.getPackage());
            Assertions.assertTrue(s.contains("line$"));
        });

        Assertions.assertEquals("", StringUtils.localMessageAtLineOfClass(null, null));
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

    private static class FieldsString {

        public static int size = 0;
        private String name;
        private int age;

    }
}
