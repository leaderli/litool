package io.leaderli.litool.core.lang;

import java.util.function.Supplier;


/**
 * 仅执行一次实际函数的函数代理，其执行结果会缓存起来，其返回结果都是同一个
 *
 * @param <T> 输出类型
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
