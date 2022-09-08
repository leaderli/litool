package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * a tool provide  if-then, case-then, instanceof-then style  code
 * <p>
 * this interface only provide instance, test method, the apply method only can chained after test method.
 * <pre>
 *  LiIf.of(obj)._if(i-&gt;i==0).then(100).else(0);
 *  LiIf.of(obj)._case(0).then(100).else(0);
 *  LiIf.of(obj)._instanceof(Integer.class).then(100).else(0);
 * </pre>
 *
 * @param <T> the type of source value
 * @param <R> the type of target value
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiIf<T, R> extends Publisher<T, R> {


    /**
     * Return a new instance
     *
     * @param supplier provide source value
     * @param <T>      the type of source value
     * @param <R>      the type of target value
     * @return a new instance
     */
    static <T, R> LiIf<T, R> of(Supplier<T> supplier) {
        T v = supplier == null ? null : supplier.get();
        return new BeginNode<>(v);
    }

    /**
     * Return a new instance
     *
     * @param value source value
     * @param <T>   the type of source value
     * @param <R>   the type of target value
     * @return a new instance
     */
    static <T, R> LiIf<T, R> of(T value) {
        return new BeginNode<>(value);
    }

    /**
     * Return a  then action
     *
     * @param compares when source value equals any of  compares, break the chain,
     *                 and apply the next {@link  FulfillNode} to get the target value
     * @return a then action
     * @see #_if(Function)
     */
    @SuppressWarnings("unchecked")
    LiThen<T, T, R> _case(T... compares);

    /**
     * Return a  then action
     *
     * @param type when source value instanceof type, break the chain,
     *             and apply the next {@link  FulfillNode} to get the target value
     * @param <M>  the type that  predicate type of source value instanceof
     * @return a then action
     * @see #_if(Function)
     */

    <M extends T> LiThen<T, M, R> _instanceof(Class<? extends M> type);


    /**
     * Return a  then action
     *
     * @param predicate test source value , when {@code  true} break the chain,
     *                  and apply the next {@link  FulfillNode} to get the target value
     * @return a then action
     */
    LiThen<T, T, R> _if(Function<? super T, ?> predicate);

    /**
     * Return the lino of target value, if the prev predicate all failed,  will return {@link  Lino#none()}
     *
     * @return the lino of target value
     */
    default Lino<R> _else() {
        return _else((R) null);
    }

    /**
     * Return the lino of target value
     *
     * @param value if the prev predicate all failed, set value as the target value
     * @return the lino of target value
     * @see #_else(Supplier)
     */
    default Lino<R> _else(R value) {
        return _else(() -> value);
    }

    /**
     * Return the lino of target value
     *
     * @param supplier if the prev predicate all failed, get supplier value  as the target value
     * @return the lino of target value
     * @see #_else(Supplier)
     */
    Lino<R> _else(Supplier<? extends R> supplier);


}

