package io.leaderli.litool.core.exception;

import java.util.function.Function;
import java.util.function.Supplier;

public class LiAssertUtil {

    public static void assertNotRun() {
        throw new IllegalStateException(" assert not to run");
    }

    public static <T> T assertNotRunWithMsg(String msg) {
        throw new IllegalStateException(msg);
    }

    public static void assertTrue(boolean assertTrue) {

        assertTrue(assertTrue, "");
    }

    public static void assertTrue(boolean assertTrue, RuntimeException throwable) {
        if (!assertTrue) {
            throw throwable;
        }
    }

    public static void assertTrue(boolean assertTrue, Function<String, RuntimeException> throwable, String msg) {
        if (!assertTrue) {
            throw throwable.apply(msg);
        }
    }

    public static void assertNotNull(Object obj, Function<String, RuntimeException> throwable, String msg) {
        if (obj == null) {
            throw throwable.apply(msg);
        }
    }

    public static void assertTrue(boolean assertTrue, String msg) {

        if (!assertTrue) {

            throw new AssertException(msg);
        }
    }

    public static void assertTrue(boolean assertTrue, Supplier<String> supplier) {

        if (!assertTrue) {

            throw new AssertException(supplier.get());
        }
    }

    public static void assertFalse(boolean assertFalse) {

        assertFalse(assertFalse, "");
    }

    public static void assertFalse(boolean assertFalse, String msg) {

        if (assertFalse) {

            throw new AssertException(msg);
        }
    }

    public static void assertFalse(boolean assertFalse, Supplier<String> supplier) {

        if (assertFalse) {

            throw new AssertException(supplier.get());
        }
    }
}
