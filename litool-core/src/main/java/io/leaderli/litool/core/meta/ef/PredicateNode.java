package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * 一个用于对数据进行过滤的节点类，实现了 LiThen<T, T, R> 接口
 *
 * @param <T> 源数据类型
 * @param <R> 结果数据类型
 */
class PredicateNode<T, R> implements LiThen<T, T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Function<? super T, ?> filter;

    public PredicateNode(Publisher<T, R> prevPublisher, Function<? super T, ?> filter) {
        Objects.requireNonNull(filter);
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberThen(actualSubscriber));
    }


    private class SubscriberThen extends IntermediateSubscriber<T, R> {

        public SubscriberThen(Subscriber<? super T, R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {

            if (BooleanUtil.parse(filter.apply(t))) {
                this.actualSubscriber.apply(t);
            } else {
                this.actualSubscriber.next(t);
            }
        }
    }
}
