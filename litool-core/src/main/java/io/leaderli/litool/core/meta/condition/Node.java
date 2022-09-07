package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/6
 */
abstract class Node<T, R> implements LiIf<T, R> {

    /**
     * @param compares 当存在 equals 的值时执行
     * @return {@link #_if(Function)}
     */
    @Override
    @SuppressWarnings("unchecked")
    public LiThen<T, T, R> _case(T... compares) {

        return new PredicateNode<>(this, v -> {

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
     * @param type 当  值 instanceof type 时执行
     * @param <M>  type 的泛型
     * @return {@code  LiCaseThen<T, M, R>}
     * @see LiThen
     */
    @Override
    public <M extends T> LiThen<T, M, R> _instanceof(Class<? extends M> type) {
        return new InstanceOfNode<>(this, type);
    }


    /**
     * @param predicate 断言函数
     * @return 返回一个可以提供 {@link LiThen#then(Function)} 转换函数的接口类，以方便链式调用。只有当 断言函数返回为true时，才会实际调用 转换函数
     * @see BooleanUtil#parse(Object)
     */
    @Override
    public LiThen<T, T, R> _if(Function<? super T, ?> predicate) {

        return new PredicateNode<>(this, predicate);
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
        ElseNode<T, R> elseNode = new ElseNode<>(this, supplier);
        elseNode.subscribe(Subscription::request);
        return Lino.of(elseNode.getResult());
    }

}
