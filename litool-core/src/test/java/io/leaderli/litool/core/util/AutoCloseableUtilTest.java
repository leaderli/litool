package io.leaderli.litool.core.util;

import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class AutoCloseableUtilTest {

    @Test
    void closeableConsumer() {

        Assertions.assertThrows(RuntimeException.class, () -> AutoCloseableUtil.closeableConsumer(null, null));
        LiBox<Integer> of = LiBox.of(0);
        ThrowableSupplier<AutoCloseable> supplier = () -> () -> of.value(1);

        AutoCloseableUtil.closeableConsumer(supplier, at -> {
        });
        Assertions.assertEquals(1, of.value());
    }

    @Test
    void closeableFunction() {


        LiBox<Integer> of = LiBox.of(0);
        ThrowableSupplier<AutoCloseable> supplier = () -> () -> of.value(1);

        AutoCloseableUtil.closeableFunction(supplier, at -> null);
        Assertions.assertEquals(1, of.value());
    }
}
