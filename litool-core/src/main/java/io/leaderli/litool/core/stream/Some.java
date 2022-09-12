package io.leaderli.litool.core.stream;

import io.leaderli.litool.core.meta.LiBox;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/12
 */
public abstract class Some<T> implements CombineOperation<T>, UnionOperation<T>,
        PublisherLogic<T> {
    protected final PublisherLogic<T> prevPublisher;

    protected Some(PublisherLogic<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }

    @Override
    public UnionOperation<T> test(Function<T, ?> predicate) {
        return new TestSome<>(this, predicate);
    }


    @Override
    public InterOperationSink<T> not() {
        return new NotSome<>(this);
    }


    @Override
    public CombineOperation<T> and() {
        return new AndSome<>(this);
    }

    @Override
    public CombineOperation<T> or() {
        return new OrSome<>(this);
    }

    @Override
    public Boolean apply(T t) {
        LiBox<Boolean> box = LiBox.of(false);
        subscribe(new SubscriberLogic<T>() {
            @Override
            public void onSubscribe(SubscriptionLogic<T> subscription) {

                subscription.request(t);
            }

            @Override
            public void next(T t, boolean last) {
                box.value(last);
            }

            @Override
            public void onNot() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void onComplete(boolean result) {
                box.value(result);

            }
        });
        return box.value();
    }
}
