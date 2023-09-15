package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class PeriodSupplierTest {


    @Test
    void executeOnce() {


        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger num = new AtomicInteger(0);
        PeriodSupplier<Integer> oncePerPeriod = new PeriodSupplier<>(50, () -> {

            if (num.get() > 2 && RandomUtil.probability(4)) {
                if (count.incrementAndGet() % 4 != 0) {
                    return null;
                }
            }
            int result = num.incrementAndGet();
            if (result == 3) {
                throw new IllegalStateException();
            }
            return result;
        }, -1, Executors.newSingleThreadExecutor());


        Assertions.assertEquals(1, oncePerPeriod.get());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {

            executorService.submit(oncePerPeriod::get);
        }
        Assertions.assertEquals(1, oncePerPeriod.get());

        ThreadUtil.sleep(50);
        for (int i = 0; i < 10; i++) {

            executorService.submit(oncePerPeriod::get);
        }
        Assertions.assertEquals(2, oncePerPeriod.get());
        ThreadUtil.sleep(50);
        for (int i = 0; i < 10; i++) {

            executorService.submit(oncePerPeriod::get);
        }
        Assertions.assertEquals(4, oncePerPeriod.get());

        ThreadUtil.sleep(50);
        for (int i = 0; i < 10; i++) {
            executorService.submit(oncePerPeriod::get);
        }
        Assertions.assertEquals(5, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(2, oncePerPeriod.get());
//
//        AtomicInteger num2 = new AtomicInteger(0);
//        oncePerPeriod = new PeriodSupplier<>(10, () -> {
//            int result = num2.incrementAndGet();
//            if (result % 2 == 0) {
//                throw new RuntimeException();
//            }
//            return result;
//        }, -1, Executors.newSingleThreadExecutor());
//
//        Assertions.assertEquals(-1, oncePerPeriod.get());
//        Assertions.assertEquals(-1, oncePerPeriod.get());
//        Assertions.assertEquals(-1, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        Assertions.assertEquals(1, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(3, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(3, oncePerPeriod.get());
//        ThreadUtil.sleep(10);
//        Assertions.assertEquals(5, oncePerPeriod.get());
    }
}
