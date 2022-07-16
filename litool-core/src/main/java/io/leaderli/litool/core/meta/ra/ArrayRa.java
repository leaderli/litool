package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class ArrayRa<T> extends SomeRa<T> {

    private final T[] arr;

    @SuppressWarnings("unchecked")
    public ArrayRa(Iterator<? extends T> values) {

        List<T> list = new ArrayList<>();
        values.forEachRemaining(list::add);
        this.arr = (T[]) list.toArray();
    }


    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(new ArraySubscriptionRa<>(actualSubscriber, arr));
    }

    private static final class ArraySubscriptionRa<T> implements SubscriptionRa {

        private final T[] arr;
        private final SubscriberRa<? super T> actualSubscriber;


        private boolean canceled;

        public ArraySubscriptionRa(SubscriberRa<? super T> actualSubscriber, T[] arr) {
            this.actualSubscriber = actualSubscriber;
            this.arr = arr;
        }

        @Override
        public void request() {
            if (canceled) {
                return;
            }


            for (T t : arr) {

                Lino.of(t).nest(actualSubscriber::next);
                // 通过 onSubscribe 将 Subscription 传递给订阅者，由订阅者来调用 cancel方法从而实现提前结束循环
                if (canceled) {
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



