package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public
class CaseIf<T, M, R> implements LiIf<T, R> {

private final PublisherIf<T, R> prevPublisher;
private final Function<? super M, ? extends R> mapper;


public CaseIf(PublisherIf<T, R> prevPublisher, Function<? super M, ? extends R> mapper) {
    this.prevPublisher = prevPublisher;
    this.mapper = mapper;
}


public void subscribe(SubscriberIf<T, R> actualSubscriber) {
    prevPublisher.subscribe(new SubscriberCaseIf(actualSubscriber));

}

private class SubscriberCaseIf extends IntermediateSubscriberIf<T, R> {

    public SubscriberCaseIf(SubscriberIf<T, R> actualSubscriber) {
        super(actualSubscriber);

    }


    /**
     * 对实际值进行断言，如果满足，值执行转换函数，并将结果保存，并终止执行，
     *
     * @param t         实际值，该值一定 instanceof M
     * @param predicate 断言函数，此处一定为 {@link IfInstanceOfThen.CaseWhenSubscriberIf#next(Object, Function)}
     * @see #mapper
     * @see BooleanUtil#parse(Object)
     */
    @SuppressWarnings({"unchecked", "JavadocReference"})
    @Override
    public void next(T t, Function<? super T, ?> predicate) {

        if (t != null && BooleanUtil.parse(predicate.apply(t))) {
            this.onComplete(mapper.apply((M) t));
        } else {
            this.actualSubscriber.next(t, null);
        }
    }

}
}
