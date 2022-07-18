package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.HashSet;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class SetRa<T> extends DefaultSomeRa<T> {


    protected SetRa(PublisherRa<T> prevPublisher) {
        super(prevPublisher);
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new SetSubscriberRa<>(actualSubscriber));


    }

    private static class SetSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {

        private Set<T> set = new HashSet<>();

        private SetSubscriberRa(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(Lino<? extends T> lino) {

            lino.map(set::add)
                    .filter()
                    .ifPresent(v -> this.actualSubscriber.next(lino));

        }

    }
}
