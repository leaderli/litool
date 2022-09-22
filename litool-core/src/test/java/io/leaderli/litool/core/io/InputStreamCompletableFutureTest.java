package io.leaderli.litool.core.io;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/9/22 8:12 PM
 */
class InputStreamCompletableFutureTest {


    @Test
    void test() {


        List<Byte> bytes = new ArrayList<>();

        Lira<Integer> range = Lira.range();
        InputStreamCompletableFuture fu = new InputStreamCompletableFuture(new MockInfiniteInputStream(() -> {

            if (bytes.isEmpty()) {
                byte[] bytes1 = StringUtils.join(",", range.limit(RandomUtil.nextInt(10, 20))).getBytes();
                for (byte b : Lira.of(CollectionUtils.toWrapperArray(bytes1)).cast(Byte.class)) {
                    bytes.add(b);
                    ThreadUtil.sleep(10);
                }
                bytes.add((byte) '\n');
                if (RandomUtil.nextInt(3) == 1) {
                    bytes.add((byte) -1);
                }
            }

            return Integer.valueOf(bytes.remove(0));
        }));

        fu.run();
        Assertions.assertTrue(fu.read().length() > 0);

    }

}
