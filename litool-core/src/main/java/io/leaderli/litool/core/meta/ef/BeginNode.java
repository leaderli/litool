package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;

import java.util.function.Supplier;

/**
 * 链式操作的开始节点，它将保存源值。
 *
 * @param <T> 源值的类型
 * @param <R> 目标值的类型
 * @see LiIf#of(Supplier)
 * @see LiIf#of(Object)
 */
public class BeginNode<T, R> extends Node<T, R> {
    private final T value;


    /**
     * @param value 源值
     */
    public BeginNode(T value) {
        this.value = value;
    }

    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {


        actualSubscriber.onSubscribe(new IntermediateSubscriber<T, R>(actualSubscriber) {

            @Override
            public void request() {

                if (value == null) {
                    actualSubscriber.onComplete(null);
                } else {
                    actualSubscriber.next(value);
                }

            }
        });
    }

}
