package io.leaderli.litool.core.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class RuntimeExceptionTransferTest {

    @Test
    public void run() {

        long begin = System.currentTimeMillis();
        RuntimeExceptionTransfer.run(() -> TimeUnit.SECONDS.sleep(1));
        long end = System.currentTimeMillis();
        assert end - begin > 1000;
        Throwable thrown = Assertions.assertThrows(RuntimeException.class, () -> RuntimeExceptionTransfer.run(() -> {
            throw new RuntimeException("hello");
        })).getCause();
        Assertions.assertEquals("hello", thrown.getLocalizedMessage());

    }

    @Test
    public void get() {

        Integer value = RuntimeExceptionTransfer.get(() -> 1);

        Assertions.assertEquals(value, 1);

        Throwable cause = Assertions.assertThrows(RuntimeException.class, () -> RuntimeExceptionTransfer.get(() -> {
            int a = 1;
            //noinspection divzero
            return a / 0;
        })).getCause();

        Assertions.assertEquals("/ by zero", cause.getMessage());
    }

    @Test
    public void accept() {

        RuntimeExceptionTransfer.accept(a -> {
        }, 1);


        Throwable cause = Assertions.assertThrows(RuntimeException.class, () -> RuntimeExceptionTransfer.accept(a -> a = a / (a - 1), 1)).getCause();

        Assertions.assertEquals("/ by zero", cause.getMessage());
    }

    @Test
    public void apply() {

        Integer apply = RuntimeExceptionTransfer.apply(a -> a, 1);
        Assertions.assertEquals(apply, 1);


        Throwable cause = Assertions.assertThrows(RuntimeException.class, () -> RuntimeExceptionTransfer.apply(a -> a / (a - 1), 1)).getCause();

        Assertions.assertEquals("/ by zero", cause.getMessage());
    }

}
