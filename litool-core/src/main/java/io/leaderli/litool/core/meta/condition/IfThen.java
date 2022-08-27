package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 * <p>
 * 后接 then 系列操作
 */
public class IfThen<T, R> implements LiThen<T, R> {
private final PublisherIf<T, R> prevPublisher;
private final Function<? super T, ?> filter;

public IfThen(PublisherIf<T, R> prevPublisher, Function<? super T, ?> filter) {
    this.prevPublisher = prevPublisher;
    this.filter = filter;
}

@Override
public void subscribe(SubscriberIf<T, R> actualSubscriber) {
    prevPublisher.subscribe(new SubscriberIfThen(actualSubscriber));

}

private class SubscriberIfThen extends IntermediateSubscriberIf<T, R> {

    public SubscriberIfThen(SubscriberIf<T, R> actualSubscriber) {
        super(actualSubscriber);

    }


    @Override
    public void next(T t, Function<? super T, ?> predicate) {

        this.actualSubscriber.next(t, filter);
    }

}
}
