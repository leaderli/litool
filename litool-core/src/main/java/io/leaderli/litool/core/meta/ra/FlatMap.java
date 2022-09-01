package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class FlatMap<T, R> extends Some<R> {


    private final Function<? super T, Iterator<? extends R>> mapper;
    private final Publisher<T> prevPublisher;

    public FlatMap(Publisher<T> prevPublisher, Function<? super T, Iterator<? extends R>> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new FlatMapSubscriber(actualSubscriber));

    }


    class FlatMapSubscriber extends IntermediateSubscriber<T, R> {

        private FlatArraySome<R> flatArraySome;

        @Override
        public void request() {
            if (flatArraySome == null) {
                super.request();
            } else {
                flatArraySome.request();
            }
        }

        @Override
        public Subscription prevSubscription() {
            return this;
        }

        private FlatMapSubscriber(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void onComplete() {
            if (this.flatArraySome == null) {
                this.actualSubscriber.onComplete();
            } else {
                this.flatArraySome = null;
            }
        }

        @Override
        public void next(T t) {

            Iterator<? extends R> iterator = mapper.apply(t);

            if (iterator != null) {

                flatArraySome = new FlatArraySome<>(this, actualSubscriber, iterator);
                flatArraySome.request();
            }


        }

        @Override
        public void onNull() {

            // flat null  will return empty
        }

    }
}
