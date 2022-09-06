package io.leaderli.litool.core.meta.ra;

import java.util.function.Function;

/**
 * 将元素类型转换为另外一个类型
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class MapRa<T, R> extends Ra<R> {
    private final Function<? super T, ? extends R> mapper;
    private final Publisher<T> prevPublisher;

    public MapRa(Publisher<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberSubscription(actualSubscriber));

    }

    private class MapSubscriberSubscription extends IntermediateSubscriberSubscription<T, R> {


        private MapSubscriberSubscription(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            if (mapper == null) {
                this.actualSubscriber.next_null();
            } else {
                SubscriberUtil.next(this.actualSubscriber, mapper.apply(t));
            }
        }

    }
}
