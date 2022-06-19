package io.leaderli.litool.core.exception;

import java.util.function.Supplier;

public class LiAssertUtil {

    public static void assertTrue(boolean assertTrue) {

        assertTrue(assertTrue, "");
    }

    public static void assertTrue(boolean assertTrue, String msg) {

        if (!assertTrue) {

            throw new LiAssertException(msg);
        }
    }

    public static void assertTrue(boolean assertTrue, Supplier<String> supplier) {

        if (!assertTrue) {

            throw new LiAssertException(supplier.get());
        }
    }

    public static void assertFalse(boolean assertFalse) {

        assertFalse(assertFalse, "");
    }

    public static void assertFalse(boolean assertFalse, String msg) {

        if (assertFalse) {

            throw new LiAssertException(msg);
        }
    }

    public static void assertFalse(boolean assertFalse, Supplier<String> supplier) {

        if (assertFalse) {

            throw new LiAssertException(supplier.get());
        }
    }
}
