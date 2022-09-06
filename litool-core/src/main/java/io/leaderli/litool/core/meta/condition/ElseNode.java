package io.leaderli.litool.core.meta.condition;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/6
 */
class ElseNode<T, R> implements Publisher<T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Supplier<? extends R> supplier;
    private R result;


    ElseNode(Publisher<T, R> prevPublisher, Supplier<? extends R> supplier) {
        this.prevPublisher = prevPublisher;
        this.supplier = supplier;
    }

    public R getResult() {
        return result;
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {

        EndSubscriber end = new EndSubscriber(actualSubscriber);
        prevPublisher.subscribe(end);

    }


    private class EndSubscriber implements Subscriber<T, R>, Subscription {

        private final Subscriber<T, R> actualSubscriber;
        private Subscription prevSubscription;

        public EndSubscriber(Subscriber<T, R> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }

        @Override
        public void onSubscribe(Subscription prevSubscription) {
            this.prevSubscription = prevSubscription;

            this.actualSubscriber.onSubscribe(this);

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

        @Override
        public void request() {
            this.prevSubscription.request();
        }
    }
}
