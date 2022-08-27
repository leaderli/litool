package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class OtherThen<T, R> implements PublisherIf<T, R> {
private final PublisherIf<T, R> prevPublisher;
private final Supplier<? extends R> supplier;

public OtherThen(PublisherIf<T, R> prevPublisher, Supplier<? extends R> supplier) {
    this.prevPublisher = prevPublisher;
    this.supplier = supplier;
}

@Override
public void subscribe(SubscriberIf<T, R> actualSubscriber) {
    prevPublisher.subscribe(new SubscriberOtherThen<>(supplier, actualSubscriber));

}

private static class SubscriberOtherThen<T, R> extends IntermediateSubscriberIf<T, R> {
    private final Supplier<? extends R> supplier;

    public SubscriberOtherThen(Supplier<? extends R> supplier, SubscriberIf<T, R> actualSubscriber) {
        super(actualSubscriber);
        this.supplier = supplier;

    }


    @Override
    public void next(T t, Function<? super T, ?> predicate) {
        if (supplier != null) {
            onComplete(supplier.get());
        }
    }

}
}
