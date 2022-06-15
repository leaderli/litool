package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class LiBoxTest {

    @Test
    public void test() {

        LiBox<String> str = new LiBox<>("hello");

        new Thread(() -> {

            RuntimeExceptionTransfer.run(() -> TimeUnit.MICROSECONDS.sleep(100));
            str.value("fuck");
        }).start();

        Assertions.assertEquals(str.value(), "hello");
        RuntimeExceptionTransfer.run(() -> TimeUnit.MICROSECONDS.sleep(200));
        Assertions.assertEquals(str.value(), "fuck");


        LiBox<String> none = LiBox.None();
        assert none.lino().notPresent();
        none.value("123");
        assert none.lino().isPresent();

    }

}
