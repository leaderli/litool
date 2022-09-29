package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * the abstract class provide apply method which could chain with test interface,  when the prev test method is
 * predicated , prev will use {@link  SubscriberIf#apply(Object)} ,and the mapper under {@link  FulfillNode} will be
 * applied to target value and break chain.  otherwise will continue the next chain node by
 * {@link  SubscriberIf#next(Object)}
 *
 * @param <T> the declare type of source value
 * @param <M> the  real type of source value  {@link  LiIf#_instanceof(Class)}
 * @param <R> the type of target value*
 * @see LiIf#_if(Function)
 * @see LiIf#_if(Function)
 * @see LiIf#_case(Object[])
 */
@FunctionalInterface
public interface LiThen<T, M extends T, R> extends PublisherIf<T, R> {

    default LiIf<T, R> _then(Function<? super M, ? extends R> mapping) {

        return new FulfillNode<>(this, mapping);
    }


    default LiIf<T, R> _then(Supplier<R> supplier) {

        return new FulfillNode<>(this, v -> supplier.get());
    }


    default LiIf<T, R> _then(R value) {

        return new FulfillNode<>(this, v -> value);
    }


}
