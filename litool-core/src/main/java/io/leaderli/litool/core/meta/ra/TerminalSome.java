package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.lang.LazyFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/1
 */
final class TerminalSome<T> extends PublisherSome<T> {

    private final Function<List<T>, Iterable<T>> deliverAction;


    public TerminalSome(Publisher<T> prevPublisher, Function<List<T>, Iterable<T>> deliverAction) {
        super(prevPublisher);
        this.deliverAction = deliverAction;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new TerminalSubscriberSubscription(actualSubscriber));
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }


    private final class TerminalSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {

        private final List<T> cache = new ArrayList<>();
        private boolean terminalComplete = false;
        private boolean canceled;
        private final LazyFunction<Integer, TerminalSubscriberSubscription> lazySupplier = new LazyFunction<Integer,
                TerminalSubscriberSubscription>() {

            @Override
            protected TerminalSubscriberSubscription init(Integer bit) {
                while (!terminalComplete) {
                    prevSubscription.request(bit);
                }
                return TerminalSubscriberSubscription.this;
            }
        };

        private TerminalSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        Iterator<T> iterator;

        @Override
        public void request(int state) {

            LiraBit bit = LiraBit.of(state);
            bit.disable(LiraBit.LIMIT);
            TerminalSubscriberSubscription apply = lazySupplier.apply(bit.get());
            if (LiraBit.isTerminal(state)) {
                while (!canceled) {
                    apply.deliverToNextSubscriber();
                }
            } else {

                apply.deliverToNextSubscriber();
            }
        }

        @Override
        public void cancel() {

            // cancel  deliver
            this.canceled = true;
            this.actualSubscriber.onCancel();
        }

        @Override
        public void next_null() {
            this.cache.add(null);
        }

        @Override
        public void onComplete() {
            completeTerminal();
        }

        @Override
        public void onCancel() {
            completeTerminal();
        }

        @Override
        public void next(T t) {
            this.cache.add(t);
        }

        private void deliverToNextSubscriber() {


            if (iterator.hasNext()) {


                try {
                    T next = iterator.next();
                    SubscriberUtil.next(actualSubscriber, next);

                } catch (Throwable throwable) {
                    actualSubscriber.onError(throwable, this);
                    actualSubscriber.next_null();
                }


            } else {

                actualSubscriber.onComplete();
                canceled = true;
            }
        }

        void completeTerminal() {

            this.terminalComplete = true;
            iterator = cache.iterator();

            if (deliverAction != null) {
                iterator = deliverAction.apply(cache).iterator();
            }
        }


    }

}
