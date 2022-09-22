package io.leaderli.litool.core.io;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.RandomUtil;
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
        InputStreamCompletableFuture fu = new InputStreamCompletableFuture(new MockInfiniteInputStream(10, () -> {

            if (bytes.isEmpty()) {
                byte[] bytes1 = range.limit(RandomUtil.nextInt(10, 20)).toString().getBytes();
                for (byte b : Lira.of(bytes1).flatMap().cast(Byte.class)) {
                    bytes.add(b);
                }
                bytes.add((byte) '\n');
            }
            return Integer.valueOf(bytes.remove(0));
        }));

        fu.run();

    }

}
