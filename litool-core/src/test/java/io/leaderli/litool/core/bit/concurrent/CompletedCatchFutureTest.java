package io.leaderli.litool.core.bit.concurrent;

import io.leaderli.litool.core.concurrent.CatchFuture;
import io.leaderli.litool.core.concurrent.CompletedCatchFuture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/9/24
 */
class CompletedCatchFutureTest {

    @Test
    void test() {

        CatchFuture<Integer> future = CompletedCatchFuture.withResult(1, null);

        Assertions.assertTrue(future.isDone());
        Assertions.assertFalse(future.isCancelled());
        Assertions.assertFalse(future.cancel(true));
        Assertions.assertEquals(1, future.get());
        Assertions.assertEquals(1, future.get(1, TimeUnit.MILLISECONDS));

        future = new CompletedCatchFuture<>(1, new NullPointerException());
        Assertions.assertThrows(RuntimeException.class, future::get);

    }

}
