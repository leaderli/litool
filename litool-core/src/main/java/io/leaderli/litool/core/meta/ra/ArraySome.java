package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class ArraySome<T> extends Some<T> {

    private final Iterable<? extends T> arr;

    public ArraySome(Iterable<? extends T> values) {
        this.arr = values;
    }


    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(new ArraySubscription(actualSubscriber));
    }

    private final class ArraySubscription implements Subscription {

        private final Subscriber<? super T> actualSubscriber;


        private boolean canceled;

        public ArraySubscription(Subscriber<? super T> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }

        @Override
        public void request() {
            if (canceled) {
                actualSubscriber.onComplete();
                return;
            }

            for (T t : arr) {


                try {
                    SubscriberUtil.next(actualSubscriber, t);
                } catch (Throwable throwable) {
                    actualSubscriber.onError(throwable, this);
                }
                // 通过 onSubscribe 将 Subscription 传递给订阅者，由订阅者来调用 cancel方法从而实现提前结束循环
                if (canceled) {
                    actualSubscriber.onComplete();

                    return;
                }

            }

            actualSubscriber.onComplete();
        }

        @Override
        public void cancel() {
            canceled = true;
        }
    }


}



