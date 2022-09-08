package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;

import java.util.function.Supplier;

/**
 * the begin node of chain, it will hold the source value
 *
 * @param <T> the type of source value
 * @param <R> the type pf target value
 * @see LiIf#of(Supplier)
 * @see LiIf#of(Object)
 */
public class BeginNode<T, R> extends Node<T, R> {
    private final T value;


    public BeginNode(T value) {
        this.value = value;
    }

    @Override
    public void subscribe(SubscriberIf<? super T, R> actualSubscriber) {


        actualSubscriber.onSubscribe(new IntermediateSubscriber<T, R>(actualSubscriber) {

            @Override
            public void request() {

                if (value == null) {
                    actualSubscriber.onComplete(null);
                } else {
                    actualSubscriber.next(value);
                }

            }
        });
    }

}
