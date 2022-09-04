package io.leaderli.litool.core.lang;

import java.util.function.Supplier;

/**
 * * a supplier proxy that provide a one-time execution of  the actual supplier
 *
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public class DisposableSupplierProxy<T> implements Supplier<T> {

    private final Supplier<T> actual;
    private T value;
    private boolean init;

    public DisposableSupplierProxy(Supplier<T> actual) {
        this.actual = actual;
    }

    public static <T> DisposableSupplierProxy<T> of(Supplier<T> actual) {
        return new DisposableSupplierProxy<>(actual);
    }

    @Override
    public final T get() {
        if (!init) {
            init = true;
            value = actual.get();
        }
        return value;
    }


}
