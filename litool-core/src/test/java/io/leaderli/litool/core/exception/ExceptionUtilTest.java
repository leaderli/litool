package io.leaderli.litool.core.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/7
 */
class ExceptionUtilTest {
    @SuppressWarnings("CaughtExceptionImmediatelyRethrown")
    @Test
    void testUnwrapThrowable() throws NoSuchMethodException {


        Method m0 = Bean1.class.getDeclaredMethod("m0");
        m0.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            try {
                m0.invoke(new Bean1());
            } catch (Throwable t) {
                throw t;
            }
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            try {
                m0.invoke(new Bean1());
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        });
    }

    @Test
    void getCause() throws NoSuchMethodException {
        try {
            test();
        } catch (Throwable throwable) {

            Throwable cause = ExceptionUtil.getCause(throwable);
            Assertions.assertEquals("test", cause.getStackTrace()[0].getMethodName());
            Assertions.assertInstanceOf(ArithmeticException.class, cause);
            Assertions.assertTrue(ExceptionUtil.beauty(throwable).contains(" java.lang.ArithmeticException: / by zero"));
        }

        try {
            test2();
        } catch (Throwable throwable) {

            Throwable cause = ExceptionUtil.getCause(throwable, getClass());
            Assertions.assertEquals("test2", cause.getStackTrace()[0].getMethodName());
            Assertions.assertInstanceOf(RuntimeException.class, cause);
        }

        Method me = ExceptionUtilTest.class.getDeclaredMethod("test");
        Runnable runnable = () -> {
            try {
                me.invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };

        try {
            runnable.run();
        } catch (Throwable throwable) {

            Throwable cause = ExceptionUtil.getCause(throwable);
            Assertions.assertEquals("test", cause.getStackTrace()[0].getMethodName());
            Assertions.assertInstanceOf(ArithmeticException.class, cause);
        }

    }

    @SuppressWarnings({"divzero", "NumericOverflow"})
    void test() {


        int a = 1 / 0;


    }

    void test2() {

        try {

            test();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Test
    void getStackTrace() {

        try {

            throw new RuntimeException();
        } catch (Throwable throwable) {

            String stackTrace = ExceptionUtil.getStackTrace(throwable);
            Assertions.assertTrue(stackTrace.startsWith("java.lang.RuntimeException"));
        }
    }


    @Test
    void supplier() {
        Assertions.assertEquals(1, ExceptionUtil.supplier(() -> 1 / (Integer.parseInt("1") - 1), 1));
        Assertions.assertEquals(1, ExceptionUtil.supplier(() -> 1 / (Integer.parseInt("1") - 1), () -> 1));
        Assertions.assertEquals(1, (Integer) ExceptionUtil.function(i -> 1 / i, 1, i -> 1));
        Assertions.assertEquals(1, ExceptionUtil.function(i -> 1 / i, 1, 1));

    }

    public static class Bean1 {

        private int m0() {
            throw new IllegalArgumentException();
        }

    }

}
