package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class DisposableSupplierProxyTest {

    @Test
    void get() {


        LiBox<Integer> count = LiBox.of(0);

        Supplier<Integer> supplier = () -> count.value() + 1;
        Assertions.assertEquals(0, count.value());
        count.value(supplier.get());
        Assertions.assertEquals(1, count.value());
        count.value(supplier.get());
        Assertions.assertEquals(2, count.value());
        supplier = DisposableSupplierProxy.of(supplier);
        count.value(supplier.get());
        Assertions.assertEquals(3, count.value());
        count.value(supplier.get());
        Assertions.assertEquals(3, count.value());
    }
}
