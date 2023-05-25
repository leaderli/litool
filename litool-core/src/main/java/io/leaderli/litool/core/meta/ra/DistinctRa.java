package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.lang.CompareDecorator;
import io.leaderli.litool.core.lang.EqualComparator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class DistinctRa<T> extends RaWithPrevPublisher<T> {
    private final EqualComparator<? super T> comparator;

    public DistinctRa(PublisherRa<T> prevPublisher, EqualComparator<? super T> comparator) {
        super(prevPublisher);
        this.comparator = comparator;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        if (comparator == null) {
            prevPublisher.subscribe(new DistinctSubscriberSubscription<>(actualSubscriber));
        } else {
            prevPublisher.subscribe(new ComparatorDistinctSubscriberSubscription<>(actualSubscriber, comparator));
        }
    }

    private static final class ComparatorDistinctSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {
        private final Set<CompareDecorator<T>> set = new LinkedHashSet<>();
        private final EqualComparator<? super T> comparator;

        private ComparatorDistinctSubscriberSubscription(SubscriberRa<? super T> actualSubscriber, EqualComparator<? super T> comparator) {
            super(actualSubscriber);
            this.comparator = comparator;
        }


        @Override
        public void next_null() {
            if (set.add(new CompareDecorator<>(null, comparator))) {
                this.actualSubscriber.next_null();
            }
        }

        @Override
        public void next(T t) {
            boolean add = set.add(new CompareDecorator<>(t, comparator));
            if (add) {
                this.actualSubscriber.next(t);
            }
        }
    }

    private static final class DistinctSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {

        private final Set<T> set = new LinkedHashSet<>();

        private DistinctSubscriberSubscription(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next_null() {
            if (set.add(null)) {
                this.actualSubscriber.next_null();
            }
        }

        @Override
        public void next(T t) {
            if (set.add(t)) {
                this.actualSubscriber.next(t);
            }
        }
    }


}
