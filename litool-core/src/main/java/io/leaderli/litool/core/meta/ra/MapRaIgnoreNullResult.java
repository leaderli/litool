package io.leaderli.litool.core.meta.ra;

import java.util.Objects;
import java.util.function.Function;

/**
 * if request is null, call {@link  SubscriberRa#next_null()}
 * if the value of {@link #mapper} is null, drop the element
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class MapRaIgnoreNullResult<T, R> extends Ra<R> {
    private final Function<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;

    public MapRaIgnoreNullResult(PublisherRa<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberSubscription(actualSubscriber));

    }

    private class MapSubscriberSubscription extends IntermediateSubscriberSubscription<T, R> {


        private MapSubscriberSubscription(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            R apply = mapper.apply(t);
            if (apply != null) {
                this.actualSubscriber.next(apply);
            }
        }
    }
}
