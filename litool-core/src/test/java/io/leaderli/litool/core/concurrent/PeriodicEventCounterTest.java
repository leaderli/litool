package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PeriodicEventCounterTest {

    @Test
    void test() {

        LiBox<Integer> count = LiBox.none();
        PeriodicEventCounter periodicEventCounter = new PeriodicEventCounter(1, () -> count.value(1), 100);

        Assertions.assertTrue(count.absent());
        periodicEventCounter.count();
        Assertions.assertTrue(count.present());

        count.reset();
        periodicEventCounter.checkTrigger();
        Assertions.assertTrue(count.present());
        count.reset();
        ThreadUtil.sleep(110);
        periodicEventCounter.checkTrigger();
        Assertions.assertTrue(count.absent());

        periodicEventCounter = new PeriodicEventCounter(2, () -> count.value(1), 100);
        Assertions.assertTrue(count.absent());
        periodicEventCounter.count();
        Assertions.assertTrue(count.absent());
        periodicEventCounter.count();
        Assertions.assertTrue(count.present());
        count.reset();
        ThreadUtil.sleep(110);
        periodicEventCounter.count();
        Assertions.assertTrue(count.absent());
    }


}
