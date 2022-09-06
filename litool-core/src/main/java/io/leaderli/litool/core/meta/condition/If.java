package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/6
 */
abstract class If<T, R> implements LiIf<T, R> {

    /**
     * @param compares 当存在 equals 的值时执行
     * @return {@link #_if(Function)}
     */
    @Override
    @SuppressWarnings("unchecked")
    public LiThen<T, R> _case(T... compares) {

        return new IfThen<>(this, v -> {

            if (compares != null) {
                for (T compare : compares) {
                    if (v.equals(compare)) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    /**
     * @param compare 当值 equals 时执行
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_case(Object)
     * @see LiThen#then(Function)
     */
    @Override
    public LiIf<T, R> _case(T compare, Function<? super T, ? extends R> mapping) {
        return _case(compare).then(mapping);
    }

    /**
     * @param compare 当值 equals 时执行
     * @return {@link #_if(Function)}
     */
    @Override
    public LiThen<T, R> _case(T compare) {
        return new IfThen<>(this, v -> v.equals(compare));
    }

    /**
     * @param type    当  值 instanceof type 时执行
     * @param <M>     type 的泛型
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_instanceof(Class)
     * @see LiInstanceOfThen#then(Function)
     */

    @Override
    public <M> LiIf<T, R> _instanceof(Class<M> type, Function<? super M, ? extends R> mapping) {
        return _instanceof(type).then(mapping);

    }

    /**
     * @param type 当  值 instanceof type 时执行
     * @param <M>  type 的泛型
     * @return {@code  LiCaseThen<T, M, R>}
     * @see LiInstanceOfThen
     */
    @Override
    public <M> LiInstanceOfThen<T, M, R> _instanceof(Class<? extends M> type) {
        return new InstanceOfThen<>(this, type);
    }

    /**
     * @param predicate 断言函数
     * @param mapping   转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_if(Function)
     * @see LiThen#then(Function)
     */
    @Override
    public LiIf<T, R> _if(Function<? super T, Object> predicate, Function<? super T, ? extends R> mapping) {

        return _if(predicate).then(mapping);
    }

    /**
     * @param predicate 断言函数
     * @return 返回一个可以提供 {@link LiThen#then(Function)} 转换函数的接口类，以方便链式调用。只有当 断言函数返回为true时，才会实际调用 转换函数
     * @see BooleanUtil#parse(Object)
     */
    @Override
    public LiThen<T, R> _if(Function<? super T, ?> predicate) {

        return new IfThen<>(this, predicate);
    }

    /**
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接执行 runnable
     *
     * @param runnable 当所有前置断言全部失败时的时执行，该出恒返回 {@link Lino#none()}
     * @return 触发实际链条执行的函数
     */
    @Override
    public Lino<R> _else(Runnable runnable) {
        OtherPublisher<T, R> otherPublisher = new OtherPublisher<>(this, () -> {
            runnable.run();
            return null;
        });
        LiBox<R> result = LiBox.none();
        End<T, R> endIf = new End<>(result::value);
        endIf.request(otherPublisher);
        return result.lino();
    }

    /**
     * @param value 当所有前置断言全部失败时的默认值
     * @return {@link #_else(Supplier)}
     */
    @Override
    public Lino<R> _else(R value) {
        return _else(() -> value);
    }

    /**
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接使用默认值提供者
     *
     * @param supplier 当所有前置断言全部失败时的默认值提供者
     * @return 触发实际链条执行的函数
     */
    @Override
    public Lino<R> _else(Supplier<? extends R> supplier) {
        OtherPublisher<T, R> otherPublisher = new OtherPublisher<>(this, supplier);
        LiBox<R> result = LiBox.none();
        End<T, R> endIf = new End<>(result::value);
        endIf.request(otherPublisher);
        return result.lino();
    }

    /**
     * else 不做任何动作，仅用于触发函数调用
     *
     * @return {@link #_else(Supplier)} 传递参数为 null
     */
    @Override
    public Lino<R> _else() {
        return _else((Supplier<? extends R>) null);
    }
}
