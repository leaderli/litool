package io.leaderli.litool.core.meta.logic;

/**
 * 逻辑运算 not 操作的实现类
 *
 * @param <T> Publisher 发布的数据类型
 * @author leaderli
 * @since 2022/9/12
 */
class NotSome<T> extends LogicSome<T> {
    public NotSome(Publisher<T> prePublisher) {
        super(prePublisher);
    }

    @Override
    public void subscribe(Subscriber<T> actualSubscriber) {
        prevPublisher.subscribe(new NotSubscriberSubscription(actualSubscriber));

    }

    private class NotSubscriberSubscription extends IntermediateSubscriberSubscription<T> {


        protected NotSubscriberSubscription(Subscriber<T> actualSubscriber) {
            super(actualSubscriber);
        }

        /**
         * 接收到数据时的操作，先调用实际订阅者的 onNot 方法，然后调用父类的 next 方法
         *
         * @param t         数据
         * @param lastState 是否为最后一个数据
         */
        @Override
        public void next(T t, boolean lastState) {

            this.actualSubscriber.onNot();
            super.next(t, lastState);
        }
    }

}
