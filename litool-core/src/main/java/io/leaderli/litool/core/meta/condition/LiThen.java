package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * test next node is the result node
 *
 * @param <T> the declare type of source value
 * @param <M> the  real type of source value  {@link  LiIf#_instanceof(Class)}
 * @param <R> the type of target value*
 * @see LiIf#_if(Function)
 * @see LiIf#_if(Function)
 * @see LiIf#_case(Object[])
 */
public abstract class LiThen<T, M extends T, R> implements Publisher<T, R> {

    public LiIf<T, R> then(Function<? super M, ? extends R> mapping) {

        return new FulfillNode<>(this, mapping);
    }


    public LiIf<T, R> then(Supplier<R> supplier) {

        return new FulfillNode<>(this, v -> supplier.get());
    }


    public LiIf<T, R> then(R value) {

        return new FulfillNode<>(this, v -> value);
    }


}
