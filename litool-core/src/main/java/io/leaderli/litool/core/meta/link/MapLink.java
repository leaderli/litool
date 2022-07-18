package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.SomeLink;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class MapLink<T, R> extends SomeLink<R> {


    private final PublisherLink<T> prevPublisher;
    private final Function<? super T, ? extends R> mapper;


    public MapLink(PublisherLink<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        super(null);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberLink<R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberLink(actualSubscriber));
    }


    private class MapSubscriberLink implements SubscriberLink<T>, SubscriptionLink<R> {

        private final SubscriberLink<R> actualSubscriber;
        private SubscriptionLink<T> prevSubscription;

        private R newValue;

        public MapSubscriberLink(SubscriberLink<R> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }

        @Override
        public void onSubscribe(SubscriptionLink<T> prevSubscription) {
            this.prevSubscription = prevSubscription;
            actualSubscriber.onSubscribe(this);
        }

        @Override
        public void next(T value) {

            Lino.of(newValue)
                    .or(mapper.apply(value))
                    .ifPresent(actualSubscriber::next)
                    .ifAbsent(() -> actualSubscriber.onCancel(Lino.none()));

        }

        @Override
        public void request() {
            this.prevSubscription.request();
        }

        @Override
        public void request(R newValue) {
            this.newValue = newValue;
            this.prevSubscription.request();
        }

        @Override
        public void onCancel(Lino<T> value) {
            this.actualSubscriber.onCancel(Lino.of(newValue).or(value.map(mapper)));
        }
    }
}
