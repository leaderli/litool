package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Supplier;

/**
 * ElseNode是责任链模式的最终节点，它持有目标值并驱动链执行
 *
 * @param <T> 源值的类型
 * @param <R> 目标值的类型
 * @see LiIf#_else(Supplier)
 * @see LiIf#_else(Object)
 * @see LiIf#_else()
 * @since 2022/9/6
 */
class ElseNode<T, R> implements Publisher<T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Supplier<? extends R> supplier;
    private R result;


    ElseNode(Publisher<T, R> prevPublisher, Supplier<? extends R> supplier) {
        this.prevPublisher = prevPublisher;
        this.supplier = supplier;
    }

    public Lino<R> getResult() {
        return Lino.of(result);
    }

    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {
        prevPublisher.subscribe(new EndSubscriber(actualSubscriber));
    }


    private class EndSubscriber extends IntermediateSubscriber<T, R> {


        protected EndSubscriber(Subscriber<? super T, R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            result = supplier.get();
        }

        @Override
        public void onComplete(R value) {
            if (value == null) {
                result = supplier.get();
            } else {
                result = value;
            }
        }

    }
}
