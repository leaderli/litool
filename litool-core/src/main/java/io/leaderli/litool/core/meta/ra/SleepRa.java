package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;
import io.leaderli.litool.core.util.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see BooleanUtil#parse(Object)
 * @since 2022/6/27
 */
class SleepRa<T> extends RaWithPrevPublisher<T> {


    /**
     * take a sleep at every countdown  element
     */
    private final int final_countdown;
    private final long milliseconds;

    public SleepRa(PublisherRa<T> prevPublisher, int countdown, long milliseconds) {
        super(prevPublisher);
        LiAssertUtil.assertTrue(countdown > 0 && milliseconds > 0);
        this.final_countdown = countdown;
        this.milliseconds = milliseconds;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberSubscription(actualSubscriber, final_countdown));

    }


    private final class FilterSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {
        private int countdown;

        public FilterSubscriberSubscription(SubscriberRa<? super T> actualSubscriber, int countdown) {
            super(actualSubscriber);
            this.countdown = countdown;
        }

        @Override
        public void next(T t) {
            actualSubscriber.next(t);
            sleep();

        }

        private void sleep() {
            if (--countdown == 0) {
                ThreadUtil.sleep(TimeUnit.MILLISECONDS, milliseconds);
                countdown = final_countdown;
            }
        }

        @Override
        public void next_null() {
            actualSubscriber.next_null();
            sleep();
        }
    }
}
