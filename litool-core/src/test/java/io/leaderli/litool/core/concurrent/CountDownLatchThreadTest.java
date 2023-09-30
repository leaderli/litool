package io.leaderli.litool.core.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class CountDownLatchThreadTest {


    @Test
    void test() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            new CountDownLatchThread(countDownLatch, count::incrementAndGet).start();
        }

        countDownLatch.await();

        Assertions.assertEquals(10, count.get());
        List<Runnable> runnables = new ArrayList<>();
        count = new AtomicInteger();

        for (int i = 0; i < 10; i++) {
            runnables.add(count::incrementAndGet);
        }

        CountDownLatchThread.execute(runnables);
        Assertions.assertEquals(10, count.get());


    }

}