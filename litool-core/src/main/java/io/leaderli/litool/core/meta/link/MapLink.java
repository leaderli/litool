package io.leaderli.litool.core.meta.link;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class MapLink<T, R> extends SomeLink<T, R> {


    private final Function<? super T, ? extends R> mapper;


    public MapLink(PublisherLink<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        super(prevPublisher);
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberLink<R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriber(actualSubscriber));
    }


    private class MapSubscriber extends IntermediateSubscriber<T, R> {


        protected MapSubscriber(SubscriberLink<R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {
            R apply = mapper.apply(value);
            if (apply != null) {
                this.actualSubscriber.next(apply);
            } else {
                this.actualSubscriber.onInterrupt(null);
            }
        }

        @Override
        public void onInterrupt(T value) {
            this.actualSubscriber.onInterrupt(mapper.apply(value));
        }


    }
}
