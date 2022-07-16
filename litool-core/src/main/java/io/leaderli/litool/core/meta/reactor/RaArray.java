package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class RaArray<T> extends RaSome<T> {

    private final T[] arr;

    @SuppressWarnings("unchecked")
    public RaArray(Iterator<? extends T> values) {

        List<T> list = new ArrayList<>();
        values.forEachRemaining(list::add);
        this.arr = (T[]) list.toArray();
    }


    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(new ArrayRaSubscription<>(actualSubscriber, arr));
    }


}

final class ArrayRaSubscription<T> implements RaSubscription {

    private final T[] arr;
    private final RaSubscriber<? super T> actualSubscriber;


    private boolean canceled;

    public ArrayRaSubscription(RaSubscriber<? super T> actualSubscriber, T[] arr) {
        this.actualSubscriber = actualSubscriber;
        this.arr = arr;
    }

    @Override
    public void request(int n) {
        if (canceled) {
            return;
        }

        if (n < 0 || n > arr.length) {
            n = arr.length;
        }
        for (int i = 0; i < n; i++) {


            Lino.of(arr[i]).nest(l -> actualSubscriber.next(Lino.narrow(l)));
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

