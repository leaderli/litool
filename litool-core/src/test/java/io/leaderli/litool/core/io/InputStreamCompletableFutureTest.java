package io.leaderli.litool.core.io;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.leaderli.litool.core.util.ConsoleUtil.line;
import static io.leaderli.litool.core.util.ConsoleUtil.print;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author leaderli
 * @since 2022/9/22 8:12 PM
 */
class InputStreamCompletableFutureTest {


    @Test
    void test() throws ExecutionException, InterruptedException, TimeoutException {


        List<Byte> bytes = new ArrayList<>();

        Iterator<Integer> range = Lira.range().iterator();
        InputStreamCompletableFuture fu = new InputStreamCompletableFuture(new MockInfiniteInputStream(() -> {

            if (bytes.isEmpty()) {
                Integer next = range.next();
                String str = next + ",";
                byte[] bytes1 = str.getBytes();
                for (byte b : Lira.of(CollectionUtils.toWrapperArray(bytes1)).cast(Byte.class)) {
                    bytes.add(b);
                }
            }

            ThreadUtil.sleep(RandomUtil.nextInt(20, 70));
            return Integer.valueOf(bytes.remove(0));
        }));


        while (!fu.isDone()) {


            print("fu", fu.get(500, MILLISECONDS));
            if (RandomUtil.shunt(5)) {
                fu.cancel(true);
            }
            line();
        }
//        Assertions.assertTrue(fu.read().length() > 0);
        line("end ");

    }

}
