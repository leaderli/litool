package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class DistinctRa<T> extends DefaultSomeRa<T> {

    private final EqualComparator<T> equalComparator;

    protected DistinctRa(PublisherRa<T> prevPublisher, EqualComparator<T> equalComparator) {
        super(prevPublisher);
        this.equalComparator = equalComparator;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DistinctSubscriberRa<>(actualSubscriber, equalComparator));
    }

    private static class DistinctWrapper<T> implements Comparator<T> {

        private final T value;
        private final EqualComparator<T> equalComparator;

        private DistinctWrapper(T value) {
            this(value, Object::equals);
        }

        private DistinctWrapper(T value, EqualComparator<T> equalComparator) {
            this.value = value;
            this.equalComparator = equalComparator;
        }


        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof DistinctWrapper) {
                Object value = ((DistinctWrapper<?>) obj).value;


                if (ClassUtil._instanceof(value, this.value.getClass())) {
                    return equalComparator.apply((T) value, this.value);
                }
            }
            return false;
        }


        @Override
        public int compare(T o1, T o2) {
            return 0;
        }
    }

    private static class DistinctSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {

        private final EqualComparator<T> equalComparator;

        private List<DistinctWrapper<T>> list = new ArrayList<>();

        private DistinctSubscriberRa(SubscriberRa<? super T> actualSubscriber, EqualComparator<T> equalComparator) {
            super(actualSubscriber);
            this.equalComparator = equalComparator;
        }


        @Override
        public void next(T t) {
            DistinctWrapper<T> only = new DistinctWrapper<>(t, equalComparator);
            if (!list.contains(only)) {
                list.add(only);
                this.actualSubscriber.next(t);
            }
        }

    }
}
