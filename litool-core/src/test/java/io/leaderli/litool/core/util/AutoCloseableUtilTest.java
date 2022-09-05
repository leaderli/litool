package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class AutoCloseableUtilTest {

    @Test
    void closeableConsumer() {

        LiBox<Integer> of = LiBox.of(0);
        Supplier<AutoCloseable> supplier = () -> () -> {
            of.value(1);
        };

        AutoCloseableUtil.closeableConsumer(supplier, at -> {
        });
        Assertions.assertEquals(1, of.value());
    }

    @Test
    void closeableFunction() {


        LiBox<Integer> of = LiBox.of(0);
        Supplier<AutoCloseable> supplier = () -> () -> {
            of.value(1);
        };

        AutoCloseableUtil.closeableFunction(supplier, at -> null);
        Assertions.assertEquals(1, of.value());
    }
}
