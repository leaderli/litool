package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class InstanceOfNode<T, M extends T, R> extends LiThen<T, M, R> {
    private final PredicateNode<T, R> predicateNode;

    public InstanceOfNode(PublisherIf<T, R> prevPublisher, Class<? extends M> middleType) {
        this.predicateNode = new PredicateNode<>(prevPublisher, t ->
                ClassUtil.isAssignableFromOrIsWrapper(middleType, t.getClass()));
    }

    @Override
    public void subscribe(SubscriberIf<? super T, R> actualSubscriber) {
        this.predicateNode.subscribe(actualSubscriber);
    }
}
