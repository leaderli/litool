package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.SomeLink;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class FilterLink<T> extends SomeLink<T> {
    private final PublisherLink<T> prevPublisher;


    private final Function<? super T, ?> filter;

    public FilterLink(PublisherLink<T> prevPublisher, Function<? super T, ?> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberLink<>(actualSubscriber, filter));

    }

}
