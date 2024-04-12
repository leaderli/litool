package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leaderli
 * @since 2023/5/19 10:47 AM
 */
class OncePerPeriodTest {

    @Test
    void executeOnce() {

        OncePerPeriod oncePerPeriod = new OncePerPeriod(10);

        AtomicInteger num = new AtomicInteger(0);
        Assertions.assertTrue(oncePerPeriod.shouldExecute());
        oncePerPeriod.executeOnce(num::incrementAndGet);
        oncePerPeriod.executeOnce(num::incrementAndGet);
        oncePerPeriod.executeOnce(num::incrementAndGet);
        Assertions.assertFalse(oncePerPeriod.shouldExecute());
        Assertions.assertEquals(1, num.get());
        ThreadUtil.sleep(20);
        Assertions.assertTrue(oncePerPeriod.shouldExecute());
        oncePerPeriod.executeOnce(num::incrementAndGet);
        Assertions.assertEquals(2, num.get());
    }
}
