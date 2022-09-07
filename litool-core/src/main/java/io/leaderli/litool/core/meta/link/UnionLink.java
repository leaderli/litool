package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class UnionLink<T, R> extends SomeLink<T, R> {


    private final Supplier<? extends R> supplier;


    public UnionLink(Publisher<T> prevPublisher, Supplier<? extends R> supplier) {
        super(prevPublisher);
        this.supplier = supplier;
    }

    @Override
    public void subscribe(Subscriber<R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriber(actualSubscriber));
    }


    private class MapSubscriber extends IntermediateSubscriber<T, R> {


        private R newValue;

        protected MapSubscriber(Subscriber<R> actualSubscriber) {
            super(actualSubscriber);
        }


        /**
         * 当前置节点正确执行时触发该函数，通过对值进行转换，有效的值则会进行下一轮的链条
         *
         * @param value 上一个节点传递的值
         * @see #request(Object)
         * @see #supplier
         */
        @Override
        public void next(T value) {

            Lino.of(newValue)
                    .or(supplier)
                    .ifPresent(actualSubscriber::next)
                    .ifAbsent(() -> actualSubscriber.onError(Lino.none()));

        }

        @Override
        public void onError(Lino<T> value) {
            this.actualSubscriber.onError(Lino.of(newValue).or(supplier));
        }

        @Override
        public void request(R newValue) {
            this.newValue = newValue;
            this.prevSubscription.request();
        }
    }
}
