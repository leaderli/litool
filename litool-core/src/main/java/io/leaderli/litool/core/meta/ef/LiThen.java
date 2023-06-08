package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * LiThen 接口继承了 PublisherIf 接口，提供了 _then 方法，它能够将一个函数应用到当前值（通过 apply 方法获取）上，生成一个新的结果。
 * 如果之前的测试方法返回 true，则应用 _then 中的映射函数，否则转到下一个节点
 *
 * @param <T> 源值类型
 * @param <M> 限定的源值类型
 * @param <R> 目标值类型
 * @see LiIf#_if(Function)
 * @see LiIf#_if(Function)
 * @see LiIf#_case(Object[])
 */
@FunctionalInterface
public interface LiThen<T, M extends T, R> extends Publisher<T, R> {

    /**
     * 将映射函数应用到当前值上生成新的结果
     *
     * @param mapping 映射函数
     * @return 一个新的 FulfillNode 对象
     */
    default LiIf<T, R> _then(Function<? super M, ? extends R> mapping) {
        return new FulfillNode<>(this, mapping);
    }

    /**
     * 将供应函数应用到当前值上生成一个新的结果
     *
     * @param supplier 供应函数
     * @return 一个新的 FulfillNode 对象
     */
    default LiIf<T, R> _then(Supplier<R> supplier) {
        return new FulfillNode<>(this, v -> supplier.get());
    }

    /**
     * 直接返回一个常量值作为结果
     *
     * @param value 常量值
     * @return 一个新的 FulfillNode 对象
     */
    default LiIf<T, R> _then(R value) {
        return new FulfillNode<>(this, v -> value);
    }
}
