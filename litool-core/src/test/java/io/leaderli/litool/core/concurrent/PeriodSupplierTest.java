package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class PeriodSupplierTest {

    @Test
    void executeOnce() {


        AtomicInteger num = new AtomicInteger(0);
        AtomicLong time = new AtomicLong(System.currentTimeMillis());
        PeriodSupplier<Integer> oncePerPeriod = new PeriodSupplier<>(50, () -> {
            int result = num.incrementAndGet();
            if (result % 3 == 0) {
                throw new IllegalStateException();
            }
            if (result % 5 == 0) {
                return null;
            }


            long now = System.currentTimeMillis();
            Assertions.assertTrue(now - time.get() > 50);
            return result;
        }, -1, Executors.newSingleThreadExecutor(), false, false);

        new Thread(() -> {
            for (; ; ) {
                oncePerPeriod.get();
            }
        }).start();

//        new int[]{1, 2, 4, 5}
//        int last = oncePerPeriod.get();
        for (int i = 0; i < 50; i++) {

            int now = oncePerPeriod.get();
            Assertions.assertTrue(now % 3 != 0 && now % 5 != 0);
            ThreadUtil.sleep(5);
        }
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        int thread_size = 100;
//
//        CountDownLatch countDownLatch = new CountDownLatch(thread_size);
//        for (int i = 0; i < thread_size; i++) {
//
//            executorService.submit(new CountDownLatchThread(countDownLatch, oncePerPeriod::get));
//        }
//        Assertions.assertEquals(1, oncePerPeriod.get());
//
//        countDownLatch.await();
//        ThreadUtil.sleep(50);
//        for (int i = 0; i < thread_size; i++) {
//
//            executorService.submit(new CountDownLatchThread(countDownLatch, oncePerPeriod::get));
//        }
//        Assertions.assertEquals(2, oncePerPeriod.get());
//        countDownLatch.await();
//
//        ThreadUtil.sleep(50);
//        for (int i = 0; i < thread_size; i++) {
//
//            executorService.submit(new CountDownLatchThread(countDownLatch, oncePerPeriod::get));
//        }
//        Assertions.assertEquals(4, oncePerPeriod.get());
//        countDownLatch.await();
//
//        ThreadUtil.sleep(50);
//        for (int i = 0; i < thread_size; i++) {
//            executorService.submit(new CountDownLatchThread(countDownLatch, oncePerPeriod::get));
//        }
//        Assertions.assertEquals(5, oncePerPeriod.get());

    }
}
