package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/18
 */
class FlatMap<T, R> extends Ra<R> {


    private final Function<? super T, Iterator<? extends R>> mapper;
    private final PublisherRa<T> prevPublisher;

    public FlatMap(PublisherRa<T> prevPublisher, Function<? super T, Iterator<? extends R>> mapper) {
        Objects.requireNonNull(mapper);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new FlatMapSubscriberSubscription(actualSubscriber));

    }


    class FlatMapSubscriberSubscription extends IntermediateSubscriberSubscription<T, R> {


        protected SubscriptionRa barricadeSubscription;


        private FlatMapSubscriberSubscription(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void request() {

            if (barricadeSubscription == null) {
                super.request();
            } else {
                barricadeSubscription.request();
            }
        }

        @Override
        public void next_null() {

            // not provide an iterator to init a new  barricadeSubscription
        }

        @Override
        public void onComplete() {
            if (this.barricadeSubscription == null) {
                this.actualSubscriber.onComplete();
            } else {
                this.barricadeSubscription = null;
            }
        }

        @Override
        public void next(T t) {

            Iterator<? extends R> iterator = mapper.apply(t);
            if (iterator == null || !iterator.hasNext()) {
                return;
            }
            if (barricadeSubscription == null) {
                barricadeSubscription = new BarricadeGenerator(this, actualSubscriber, iterator);
            }
            barricadeSubscription.request();


        }

        /**
         * @author leaderli
         * @since 2022/7/16
         */
        public final class BarricadeGenerator extends GeneratorSubscription<R> {


            public BarricadeGenerator(Completable mediate, SubscriberRa<? super R> actualSubscriber,
                                      Iterator<? extends R> iterator) {
                super(actualSubscriber, iterator);
            }


            @SuppressWarnings("java:S2583")
            @Override
            public void request() {

                if (completed) {
                    // just tell  barricade is completed, not tell actualSubscriber
                    onComplete();
                    return;
                }
                if (iterator.hasNext()) {
                    try {
                        SubscriberUtil.next(actualSubscriber, iterator.next());
                    } catch (Throwable throwable) {
                        actualSubscriber.next_null();
                        actualSubscriber.onError(throwable, this);
                    }
                } else {
                    completed = true;
                    onComplete();
                }
            }

        }
    }
}
