package io.leaderli.litool.core.lang;

import java.util.function.Function;

/**
 * 仅执行一次实际函数的函数代理，其执行结果会缓存起来，不论其请求参数如何变化，其返回结果都是同一个
 *
 * @param <T> 输入类型
 * @param <R> 输出类型
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
