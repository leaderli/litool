package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;

import java.util.function.Function;
import java.util.function.Supplier;


/**
 * 提供一个目标值{@link  #mapper}，仅在前一个节点的测试结果为 true 时进行操作。
 *
 * @param <T> 源值的声明类型
 * @param <M> 源值的实际类型{@link LiIf # _instanceof(Class)}
 * @param <R> 目标值的类型
 * @author leaderli
 * @see LiThen#_then(Supplier)
 * @see LiThen#_then(Function)
 * @see LiThen#_then(Object)
 * @since 2022/7/17
 */
public class FulfillNode<T, M extends T, R> extends Node<T, R> {

    private final Publisher<T, R> prevPublisher;
    private final Function<? super M, ? extends R> mapper;


    /**
     * @param prevPublisher 前一个节点
     * @param mapper        目标值的转换函数
     */
    public FulfillNode(Publisher<T, R> prevPublisher, Function<? super M, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberCase(actualSubscriber));

    }

    private class SubscriberCase extends IntermediateSubscriber<T, R> {

        public SubscriberCase(Subscriber<? super T, R> actualSubscriber) {
            super(actualSubscriber);

        }

        @SuppressWarnings("unchecked")
        @Override
        public void apply(T t) {
            R apply = mapper.apply((M) t);
            this.onComplete(apply);

        }
    }
}
