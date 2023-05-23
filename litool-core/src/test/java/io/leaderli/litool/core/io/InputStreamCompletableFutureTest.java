package io.leaderli.litool.core.io;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/9/22 8:12 PM
 */
class InputStreamCompletableFutureTest {


    @Test
    void test() {


        List<Byte> bytes = new ArrayList<>();

        Iterator<Integer> range = Lira.range().iterator();
        InputStreamCompletableFuture fu = new InputStreamCompletableFuture(new MockInfiniteInputStream(() -> {

            if (bytes.isEmpty()) {
                Integer next = range.next();
                String str = next + ",";
                byte[] bytes1 = str.getBytes();
                for (byte b : Lira.of(ArrayUtils.toArray(bytes1)).cast(Byte.class)) {
                    bytes.add(b);
                }
            }

            ThreadUtil.sleep(RandomUtil.nextInt(20, 70));
            return Integer.valueOf(bytes.remove(0));
        }));


        while (!fu.isDone()) {

            fu.get(10, TimeUnit.MILLISECONDS);
            if (RandomUtil.probability(5)) {
                fu.cancel(true);
            }
        }
        Assertions.assertTrue(fu.isDone());
        Assertions.assertThrows(CancellationException.class, fu::get);

    }

}
