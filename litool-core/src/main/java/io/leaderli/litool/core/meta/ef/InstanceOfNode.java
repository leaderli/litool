package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * 用于判断对象是否为指定类型的节点。
 *
 * @param <T> 判断对象的类型
 * @param <M> 指定的类型
 * @param <R> 订阅的结果类型
 */
class InstanceOfNode<T, M extends T, R> implements LiThen<T, M, R> {
    private final PredicateNode<T, R> predicateNode;

    public InstanceOfNode(Publisher<T, R> prevPublisher, Class<? extends M> middleType) {
        this.predicateNode = new PredicateNode<>(prevPublisher, t ->
                ClassUtil.isAssignableFromOrIsWrapper(middleType, t.getClass()));
    }

    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {
        this.predicateNode.subscribe(actualSubscriber);
    }
}
