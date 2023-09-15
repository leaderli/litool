package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leaderli
 * @since 2023/9/15 11:39 AM
 */
class SimpleFutureTest {

    @Test
    void test() throws ExecutionException, InterruptedException, TimeoutException {
        SimpleFuture<Integer> simpleFuture = new SimpleFuture<>();

        Executors.newSingleThreadExecutor().submit(()->{
            ThreadUtil.sleep(1000);
            simpleFuture.setResult(1);
        });
        System.out.println(simpleFuture.get(100, TimeUnit.MILLISECONDS));
        System.out.println(simpleFuture.get());

    }

}
