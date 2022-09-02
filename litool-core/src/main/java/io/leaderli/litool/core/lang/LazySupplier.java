package io.leaderli.litool.core.lang;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public abstract class LazySupplier<T> implements Supplier<T> {
    private T t;
    private boolean init;

    @Override
    public final T get() {
        if (!init) {
            init = true;
            t = init();
        }
        return t;
    }

    protected abstract T init();

}
