package io.leaderli.litool.core.meta.logic;

/**
 * 流的入口操作
 *
 * @param <T> 流中元素的类型
 */
class BeginSome<T> extends LogicSome<T> {
    /**
     * 构造函数
     */
    protected BeginSome() {
        super(null);
    }

    /**
     * 订阅流
     *
     * @param actualSubscriber 实际的订阅者
     */
    @Override
    public void subscribe(Subscriber<T> actualSubscriber) {
        actualSubscriber.onSubscribe(new BeginSubscriber(actualSubscriber));
    }

    /**
     * 开始订阅的订阅者
     */
    private class BeginSubscriber implements Subscriber<T>, Subscription<T> {
        protected final Subscriber<T> actualSubscriber;

        /**
         * 构造函数
         *
         * @param actualSubscriber 实际的订阅者
         */
        protected BeginSubscriber(Subscriber<T> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }

        /**
         * 订阅前的操作
         *
         * @param prevSubscription 上一个订阅者
         */
        @Override
        public final void onSubscribe(Subscription<T> prevSubscription) {
            actualSubscriber.onSubscribe(this);
        }


        @Override
        public void next(T t, boolean lastState) {
            throw new UnsupportedOperationException();
        }

        /**
         * 当没有元素时的操作
         */
        @Override
        public void onNot() {
            throw new UnsupportedOperationException();
        }

        /**
         * 当流结束时的操作
         *
         * @param result 流的结果
         */
        @Override
        public void onComplete(boolean result) {
            throw new UnsupportedOperationException();
        }

        /**
         * 请求元素
         *
         * @param value 元素
         */
        @Override
        public void request(T value) {
            this.actualSubscriber.next(value, true);
        }

    }

}
