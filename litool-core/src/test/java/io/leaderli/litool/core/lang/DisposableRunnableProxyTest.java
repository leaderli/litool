package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class DisposableRunnableProxyTest {

    @Test
    void run() {
        LiBox<Integer> box = LiBox.of(0);

        Runnable runnable = () -> box.value(box.value() + 1);

        Assertions.assertEquals(0, box.value());
        runnable.run();
        Assertions.assertEquals(1, box.value());
        runnable.run();
        Assertions.assertEquals(2, box.value());

        runnable = DisposableRunnableProxy.of(runnable);
        runnable.run();
        Assertions.assertEquals(3, box.value());
        runnable.run();
        Assertions.assertEquals(3, box.value());
        runnable.run();
        Assertions.assertEquals(3, box.value());


    }

}
