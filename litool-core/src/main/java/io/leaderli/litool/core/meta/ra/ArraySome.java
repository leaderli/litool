package io.leaderli.litool.core.meta.ra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class ArraySome<T> extends Some<T> {

private final T[] arr;

@SuppressWarnings("unchecked")
public ArraySome(Iterator<? extends T> values) {

    List<T> list = new ArrayList<>();
    values.forEachRemaining(list::add);
    this.arr = (T[]) list.toArray();
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
            return;
        }


        for (T t : arr) {

            try {

                if (t == null) {
                    actualSubscriber.next();
                } else {
                    actualSubscriber.next(t);
                }
            } catch (Throwable throwable) {
                actualSubscriber.onError(throwable, this);
            }
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



