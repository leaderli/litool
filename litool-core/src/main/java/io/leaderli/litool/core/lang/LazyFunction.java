package io.leaderli.litool.core.lang;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public abstract class LazyFunction<T, R> implements Function<T, R> {
    private R r;
    private boolean init;

    @Override
    public final R apply(T t) {
        if (!init) {
            init = true;
            r = init(t);
        }
        return r;
    }

    protected abstract R init(T t);

}
