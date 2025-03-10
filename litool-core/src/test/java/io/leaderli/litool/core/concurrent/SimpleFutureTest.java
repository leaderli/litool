package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leaderli
 * @since 2023/9/15 11:39 AM
 */
class SimpleFutureTest {

    @Test
    void test() throws InterruptedException, TimeoutException {
        SimpleFuture<Integer> simpleFuture = new SimpleFuture<>();
        simpleFuture.submit(() -> ThreadUtil.delay(30, () -> 1));
        Assertions.assertThrows(TimeoutException.class, () -> simpleFuture.get(10, TimeUnit.MILLISECONDS));
        Assertions.assertEquals(1, simpleFuture.get());
        SimpleFuture<Integer> simpleFuture2 = new SimpleFuture<>();
        simpleFuture2.submit(() -> {
            ThreadUtil.sleep(TimeUnit.DAYS, 100);
            return 2;
        });
        Assertions.assertThrows(TimeoutException.class, () -> simpleFuture2.get(10, TimeUnit.MILLISECONDS));

        SimpleFuture<Integer> simpleFuture3 = new SimpleFuture<>();

        simpleFuture3.submit(() -> {
            ThreadUtil.sleep(100);
            throw new RuntimeException();
        });

        Assertions.assertEquals(IllegalStateException.class, simpleFuture3.getException().getClass());
        Assertions.assertNull(simpleFuture3.get());
        Assertions.assertEquals(RuntimeException.class, simpleFuture3.getException().getClass());

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        SimpleFuture<Integer> simpleFuture4 = new SimpleFuture<>();
        simpleFuture4.submit(executorService, () -> 1);
        Assertions.assertEquals(1, simpleFuture4.get());


    }

}
