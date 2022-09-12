package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/7/18
 * <p>
 * 前后类型相同
 */
abstract class SameTypeIntermediateSubscriber<T> extends IntermediateSubscriber<T, T> {
    protected SameTypeIntermediateSubscriber(SubscriberLink<T> actualSubscriber) {
        super(actualSubscriber);
    }


    @Override
    public void onInterrupt(T value) {
        this.actualSubscriber.onInterrupt(value);
    }


}
