package io.leaderli.litool.core.lang;

import java.util.function.Function;

/**
 * a function proxy that provide a one-time execution of  the actual function
 *
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public class DisposableFunctionProxy<T, R> implements Function<T, R> {
    private final Function<T, R> function;
    private R r;
    private boolean init;

    public DisposableFunctionProxy(Function<T, R> function) {
        this.function = function;
    }

    public static <T, R> DisposableFunctionProxy<T, R> of(Function<T, R> function) {
        return new DisposableFunctionProxy<>(function);
    }

    @Override
    public final R apply(T t) {
        if (!init) {
            init = true;
            r = function.apply(t);
        }
        return r;
    }


}
