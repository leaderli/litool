package io.leaderli.litool.core.meta.ra;


import io.leaderli.litool.core.collection.Generator;
import io.leaderli.litool.core.exception.InfiniteException;

import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class IterableRa<T> extends Ra<T> {

    private final Iterable<? extends T> iterable;
    private final boolean infinite;

    public IterableRa(Iterable<? extends T> iterable) {
        this.iterable = iterable;
        this.infinite = iterable instanceof Generator;

    }


    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(newGenerator(actualSubscriber));
    }

    public ItrGenerator newGenerator(SubscriberRa<? super T> actualSubscriber) {
        return new ItrGenerator(actualSubscriber, iterable.iterator());
    }


    public class ItrGenerator extends GeneratorSubscription<T> {

        protected ItrGenerator(SubscriberRa<? super T> actualSubscriber, Iterator<? extends T> iterator) {
            super(actualSubscriber, iterator);
        }

        @Override
        public final void request(int state) {
            LiraBit bit = LiraBit.of(state);

            // not active response onComplete signal, only response on when the next request
            if (infinite) {
                if (LiraBit.isTerminal(state) && bit.miss(LiraBit.LIMIT)) {
                    throw new InfiniteException("generator loop \r\n\tat " + iterator);
                }
            }


            if (LiraBit.isTerminal(state)) {
                while (true) {
                    performRequest();
                    if (completed) {
                        return;
                    }
                }

            } else {
                performRequest();
            }

        }

        private void performRequest() {
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



