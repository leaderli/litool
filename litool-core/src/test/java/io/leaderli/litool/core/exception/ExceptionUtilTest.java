package io.leaderli.litool.core.exception;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/7
 */
class ExceptionUtilTest {


    @Test
    void getCause() throws NoSuchMethodException {
        try {
            test();
        } catch (Throwable throwable) {

            Throwable cause = ExceptionUtil.getCause(throwable);
            assert cause instanceof ArithmeticException;
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
            assert cause instanceof ArithmeticException;
        }

    }

    @SuppressWarnings({"divzero", "NumericOverflow"})
    void test() {


        int a = 1 / 0;


    }

}