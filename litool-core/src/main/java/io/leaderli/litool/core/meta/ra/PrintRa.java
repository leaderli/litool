package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/25 8:41 AM
 * 打印调试使用
 */
public class PrintRa<T> extends SomeRa<T> {
    private final PublisherRa<T> prevPublisher;
    private final Consumer<T> debug;

    public PrintRa(PublisherRa<T> prevPublisher, Consumer<T> debug) {
        this.prevPublisher = prevPublisher;
        this.debug = debug;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new PrintSubscriberRa(actualSubscriber));

    }

    private class PrintSubscriberRa extends IntermediateSubscriberRa<T, T> {


        private PrintSubscriberRa(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(Lino<? extends T> t) {
            t.ifPresent(debug);
            this.actualSubscriber.next(t);
        }

    }
}
