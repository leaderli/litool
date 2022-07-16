package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.LiLink;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class CancelConsumerLink<T> extends LiLink<T> {

    private final PublisherLink<T> prevPublisher;
    private final Consumer<? super T> cancelConsumer;


    public CancelConsumerLink(PublisherLink<T> prevPublisher, final Consumer<? super T> cancelConsumer) {
        this.prevPublisher = prevPublisher;
        this.cancelConsumer = cancelConsumer;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelConsumerSubscriberLink<>(actualSubscriber, cancelConsumer));
    }


}
