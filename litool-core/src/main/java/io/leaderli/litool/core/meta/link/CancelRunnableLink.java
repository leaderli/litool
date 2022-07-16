package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.SomeLink;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class CancelRunnableLink<T> extends SomeLink<T> {

    private final PublisherLink<T> prevPublisher;
    private final Runnable runnable;


    public CancelRunnableLink(PublisherLink<T> prevPublisher, final Runnable runnable) {
        this.prevPublisher = prevPublisher;
        this.runnable = runnable;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelRunnableSubscriberLink<>(actualSubscriber, runnable));
    }


}
