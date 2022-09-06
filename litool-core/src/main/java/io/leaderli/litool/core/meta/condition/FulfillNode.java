package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class FulfillNode<T, M, R> extends Node<T, R> {

    private final Publisher<T, R> prevPublisher;
    private final Function<? super M, ? extends R> mapper;


    public FulfillNode(Publisher<T, R> prevPublisher, Function<? super M, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberCase(actualSubscriber));

    }

    private class SubscriberCase extends IntermediateSubscriber<T, R> {

        public SubscriberCase(Subscriber<T, R> actualSubscriber) {
            super(actualSubscriber);

        }

        @SuppressWarnings("unchecked")
        @Override
        public void apply(T t) {
            R apply = mapper.apply((M) t);
            this.onComplete(apply);

        }
    }
}
