package io.leaderli.litool.core.meta.ra;


import java.util.Iterator;

/**
 * 迭代Ra
 *
 * @param <T> 集合类型
 * @author leaderli
 * @since 2022/7/16
 */
public class IterableRa<T> extends Ra<T> {

    private final Iterable<? extends T> iterable;

    /**
     * @param iterable 迭代器
     */
    public IterableRa(Iterable<? extends T> iterable) {
        this.iterable = iterable;

    }


    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(newGenerator(actualSubscriber));
    }

    /**
     * @param actualSubscriber 实际订阅者
     * @return -
     */
    public ItrGenerator newGenerator(SubscriberRa<? super T> actualSubscriber) {
        return new ItrGenerator(actualSubscriber, iterable.iterator());
    }


    /**
     * 一个新的迭代订阅者
     */
    public class ItrGenerator extends GeneratorSubscription<T> {

        protected ItrGenerator(SubscriberRa<? super T> actualSubscriber, Iterator<? extends T> iterator) {
            super(actualSubscriber, iterator);
        }

        @Override
        public final void request() {


            if (completed) {
                this.actualSubscriber.onComplete();
                return;
            }
            if (iterator.hasNext()) {
                // not catch iterator.next to avoid infinite loop
                T next = iterator.next();

                try {
                    SubscriberUtil.next(actualSubscriber, next);
                } catch (Throwable throwable) {
                    if (throwable instanceof LiraRuntimeException) {
                        throw new LiraRuntimeException((LiraRuntimeException) throwable);
                    }
                    actualSubscriber.next_null();
                    actualSubscriber.onError(throwable, this);
                }
            } else {
                completed = true;
                this.actualSubscriber.onComplete();
            }
        }

    }


}



