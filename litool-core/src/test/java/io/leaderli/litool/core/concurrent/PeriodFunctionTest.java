package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leaderli
 * @since 2023/9/15 9:54 AM
 */
class PeriodFunctionTest {

    @Test
    void test() {

        Map<Integer, AtomicInteger> map = new HashMap<>();
        map.put(1, new AtomicInteger(10));
        map.put(2, new AtomicInteger(20));
        PeriodFunction<Integer, Integer> periodFunction = new PeriodFunction<>(50, i -> map.get(i).incrementAndGet(), Executors.newSingleThreadExecutor());


        Assertions.assertEquals(11, periodFunction.apply(1));
        Assertions.assertEquals(11, periodFunction.apply(1));
        Assertions.assertEquals(11, periodFunction.apply(1));
        ThreadUtil.sleep(50);
        Assertions.assertEquals(12, periodFunction.apply(1));
        ThreadUtil.sleep(50);
        Assertions.assertEquals(13, periodFunction.apply(1));
        Assertions.assertEquals(21, periodFunction.apply(2));

    }
}
