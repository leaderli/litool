package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leaderli
 * @since 2023/9/15 11:39 AM
 */
class SimpleFutureTest {

    @Test
    void test() throws ExecutionException, InterruptedException {
        SimpleFuture<Integer> simpleFuture = new SimpleFuture<>();
        simpleFuture.submit(() -> ThreadUtil.delay(30, () -> 1));
        Assertions.assertThrows(TimeoutException.class, () -> simpleFuture.get(10, TimeUnit.MILLISECONDS));
        Assertions.assertEquals(1, simpleFuture.get());

    }

}
