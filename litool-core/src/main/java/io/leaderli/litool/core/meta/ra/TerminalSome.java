package io.leaderli.litool.core.meta.ra;

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

        private TerminalSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void request() {
            while (!terminalComplete) {
                prevSubscription.request();
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

        void completeTerminal() {

            this.terminalComplete = true;

            deliverToNextSubscriber();
        }

        private void deliverToNextSubscriber() {

            Iterable<T> iterable = cache;

            if (deliverAction != null) {
                iterable = deliverAction.apply(cache);
            }

            for (T t : iterable) {

                actualSubscriber.beforeRequest();
                if (canceled) {
                    return;
                }


                try {
                    SubscriberUtil.next(actualSubscriber, t);
                } catch (Throwable throwable) {
                    actualSubscriber.onError(throwable, this);
                    actualSubscriber.onNull();
                }

                if (canceled) {
                    return;
                }
                actualSubscriber.onRequested();

            }
            actualSubscriber.onComplete();
        }

        @Override
        public void next(T t) {
            this.cache.add(t);
        }


    }

}
