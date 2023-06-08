package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.meta.ef.BeginNode;
import io.leaderli.litool.core.meta.ef.FulfillNode;
import io.leaderli.litool.core.meta.ef.LiThen;
import io.leaderli.litool.core.meta.ef.Publisher;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 提供 if-then、case-then、instanceof-then 风格的代码工具，链式接口，仅在_else后才会实际执行
 * <pre>
 *  LiIf.of(obj)._if(i-&gt;i==0).then(100)._else(0);
 *  LiIf.of(obj)._case(0).then(100)._else(0);
 *  LiIf.of(obj)._instanceof(Integer.class).then(100)._else(0);
 * </pre>
 *
 * @param <T> 源值的类型
 * @param <R> 目标值的类型
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiIf<T, R> extends Publisher<T, R> {


    /**
     * 返回一个新的LiIf实例。
     *
     * @param supplier 提供源值的Supplier
     * @param <T>      源值的类型
     * @param <R>      目标值的类型
     * @return 新的LiIf实例
     */
    static <T, R> LiIf<T, R> of(Supplier<T> supplier) {
        T v = supplier == null ? null : supplier.get();
        return new BeginNode<>(v);
    }

    /**
     * 返回一个新的LiIf实例。
     *
     * @param value 源值
     * @param <T>   源值的类型
     * @param <R>   目标值的类型
     * @return 新的LiIf实例
     */
    static <T, R> LiIf<T, R> of(T value) {
        return new BeginNode<>(value);
    }

    /**
     * 返回一个then操作。
     *
     * @param compares 当源值等于任何一个compares时，终止链式操作，
     *                 并将下一个{@link FulfillNode}应用于获取目标值。
     * @return then操作
     * @see #_if(Function)
     */
    @SuppressWarnings("unchecked")
    LiThen<T, T, R> _case(T... compares);

    /**
     * 返回一个then操作。
     *
     * @param type 当源值instanceof type时，终止链式操作，
     *             并将下一个{@link FulfillNode}应用于获取目标值。
     * @param <M>  源值instanceof的谓词类型
     * @return then操作
     * @see #_if(Function)
     */

    <M extends T> LiThen<T, M, R> _instanceof(Class<? extends M> type);


    /**
     * 返回一个then操作。
     *
     * @param filter 测试源值，当 {@code  BooleanUtil.parse(filter.apply(value) == true} 时，终止链式操作，
     *               并将下一个{@link FulfillNode}应用于获取目标值。
     * @return then操作
     * @see BooleanUtil#parse(Object)
     */
    LiThen<T, T, R> _if(Function<? super T, ?> filter);


    /**
     * 返回目标值的Lino，如果之前的predicate都失败了，
     * 则返回{@link Lino#none()}。
     *
     * @return 目标值的Lino
     */
    default Lino<R> _else() {
        return _else((R) null);
    }

    /**
     * 返回目标值的Lino。
     *
     * @param value 如果之前的predicate都失败了，则将value设置为目标值。
     * @return 目标值的Lino
     * @see #_else(Supplier)
     */
    default Lino<R> _else(R value) {
        return _else(() -> value);
    }

    /**
     * 返回目标值的Lino。
     *
     * @param supplier 如果之前的predicate都失败了，则使用supplier获取的值作为目标值。
     * @return 目标值的Lino
     */
    Lino<R> _else(Supplier<? extends R> supplier);


}

