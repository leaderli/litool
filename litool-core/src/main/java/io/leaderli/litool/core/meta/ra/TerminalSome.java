package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/9/1
 */
final class TerminalSome<T> extends PublisherSome<T> {


    public TerminalSome(Publisher<T> prevPublisher) {
        super(prevPublisher);
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

        private boolean cancel;

        private TerminalSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void request() {
            while (!cancel) {
                prevSubscription.request();
            }
        }


        @Override
        public void onComplete() {
            this.cancel = true;
            actualSubscriber.onComplete();
        }

        @Override
        public void onCancel() {
            this.cancel = true;
            actualSubscriber.onCancel();
        }

        @Override
        public void next(T t) {
            this.actualSubscriber.next(t);
        }
    }

}
