package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Supplier;

/**
 * the end node of chain, it will hold the target value, and will drive chain execute
 *
 * @param <T> the type of source value
 * @param <R> the type pf target value
 * @author leaderli
 * @see LiIf#_else(Supplier)
 * @see LiIf#_else(Object)
 * @see LiIf#_else()
 * @since 2022/9/6
 */
class ElseNode<T, R> implements PublisherIf<T, R> {
    private final PublisherIf<T, R> prevPublisher;
    private final Supplier<? extends R> supplier;
    private R result;


    ElseNode(PublisherIf<T, R> prevPublisher, Supplier<? extends R> supplier) {
        this.prevPublisher = prevPublisher;
        this.supplier = supplier;
    }

    public Lino<R> getResult() {
        return Lino.of(result);
    }

    @Override
    public void subscribe(SubscriberIf<? super T, R> actualSubscriber) {
        prevPublisher.subscribe(new EndSubscriber(actualSubscriber));
    }


    private class EndSubscriber extends IntermediateSubscriber<T, R> {


        protected EndSubscriber(SubscriberIf<? super T, R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            result = supplier.get();
        }

        @Override
        public void onComplete(R value) {
            if (value == null) {
                result = supplier.get();
            } else {
                result = value;
            }
        }

    }
}
