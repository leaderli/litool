package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiIf<T, R> extends Publisher<T, R> {


    static <T, R> LiIf<T, R> of(Lino<T> lino) {
        T v = lino == null ? null : lino.get();
        return new BeginNode<>(v);
    }

    static <T, R> LiIf<T, R> of(T value) {
        return new BeginNode<>(value);
    }

    /**
     * @param compares 当存在 equals 的值时执行
     * @return {@link #_if(Function)}
     */
    @SuppressWarnings("unchecked")
    LiThen<T, R> _case(T... compares);


    /**
     * @param compare 当值 equals 时执行
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_case(Object)
     * @see LiThen#then(Function)
     */
    LiIf<T, R> _case(T compare, Function<? super T, ? extends R> mapping);

    /**
     * @param compare 当值 equals 时执行
     * @return {@link #_if(Function)}
     */
    LiThen<T, R> _case(T compare);

    /**
     * @param type    当  值 instanceof type 时执行
     * @param <M>     type 的泛型
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_instanceof(Class)
     * @see LiInstanceOfThen#then(Function)
     */

    <M> LiIf<T, R> _instanceof(Class<M> type, Function<? super M, ? extends R> mapping);


    /**
     * @param type 当  值 instanceof type 时执行
     * @param <M>  type 的泛型
     * @return {@code  LiCaseThen<T, M, R>}
     * @see LiInstanceOfThen
     */
    <M> LiInstanceOfThen<T, M, R> _instanceof(Class<? extends M> type);

    /**
     * @param predicate 断言函数
     * @param mapping   转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_if(Function)
     * @see LiThen#then(Function)
     */
    LiIf<T, R> _if(Function<? super T, Object> predicate, Function<? super T, ? extends R> mapping);

    /**
     * @param predicate 断言函数
     * @return 返回一个可以提供 {@link LiThen#then(Function)} 转换函数的接口类，以方便链式调用。只有当 断言函数返回为true时，才会实际调用 转换函数
     * @see BooleanUtil#parse(Object)
     */
    LiThen<T, R> _if(Function<? super T, ?> predicate);


    /**
     * @param value 当所有前置断言全部失败时的默认值
     * @return {@link #_else(Supplier)}
     */
    Lino<R> _else(R value);

    /**
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接使用默认值提供者
     *
     * @param supplier 当所有前置断言全部失败时的默认值提供者
     * @return 触发实际链条执行的函数
     */
    Lino<R> _else(Supplier<? extends R> supplier);

    /**
     * else 不做任何动作，仅用于触发函数调用
     *
     * @return {@link #_else(Supplier)} 传递参数为 null
     */
    Lino<R> _else();


}

