package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class InstanceOfNode<T, M extends T, R> implements LiThen<T, M, R> {
    private final PredicateNode<T, R> predicateNode;

    public InstanceOfNode(Publisher<T, R> prevPublisher, Class<? extends M> middleType) {
        this.predicateNode = new PredicateNode<>(prevPublisher, t ->
                ClassUtil.isAssignableFromOrIsWrapper(middleType, t.getClass()));
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        this.predicateNode.subscribe(actualSubscriber);
    }


}
