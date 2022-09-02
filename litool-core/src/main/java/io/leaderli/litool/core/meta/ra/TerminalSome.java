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
        prevPublisher.subscribe(new TerminalSubscriber(actualSubscriber));
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }


    private final class TerminalSubscriber extends IntermediateSubscriber<T, T> {

        private final List<T> cache = new ArrayList<>();
        private boolean terminalComplete = false;
        private boolean canceled;
        private final LazyFunction<LiraBit, TerminalSubscriber> lazySupplier = new LazyFunction<LiraBit, TerminalSubscriber>() {

            @Override
            protected TerminalSubscriber init(LiraBit bit) {
                while (!terminalComplete) {
                    prevSubscription.request(bit);
                }
                return TerminalSubscriber.this;
            }
        };

        private TerminalSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        Iterator<T> iterator;

        @Override
        public void request(LiraBit bit) {

            TerminalSubscriber apply = lazySupplier.apply(bit);
            if (bit.have(LiraBit.T_TERMINAL)) {

                System.out.println("1 " + canceled);
                while (!canceled) {
                    apply.deliverToNextSubscriber();
                }
            } else {
                System.out.println("2");

                apply.deliverToNextSubscriber();
            }
        }

        @Override
        public void cancel() {

            // cancel  deliver
            this.canceled = true;
        }

        @Override
        public void onNull() {
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

        private void deliverToNextSubscriber() {


            if (iterator.hasNext()) {

                actualSubscriber.beforeRequest();
                if (canceled) {
                    return;
                }


                try {
                    T next = iterator.next();
                    System.out.println("next: " + next);
                    SubscriberUtil.next(actualSubscriber, next);

                } catch (Throwable throwable) {
                    actualSubscriber.onError(throwable, this);
                    actualSubscriber.onNull();
                }

                System.out.println("cancel ----" + canceled);
                if (canceled) {
                    return;
                }
                actualSubscriber.onRequested();

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
//            deliverToNextSubscriber();
        }

        @Override
        public void next(T t) {
            this.cache.add(t);
        }


    }

}
