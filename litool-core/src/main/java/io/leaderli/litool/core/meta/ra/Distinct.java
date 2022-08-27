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
public class Distinct<T> extends PublisherSome<T> {

private final EqualComparator<T> equalComparator;

protected Distinct(Publisher<T> prevPublisher, EqualComparator<T> equalComparator) {
    super(prevPublisher);
    this.equalComparator = equalComparator;
}

@Override
public void subscribe(Subscriber<? super T> actualSubscriber) {
    prevPublisher.subscribe(new DistinctSubscriber<>(actualSubscriber, equalComparator));
}

private static class DistinctWrapper<T> implements Comparator<T> {

    private final T value;
    private final EqualComparator<T> equalComparator;

    private DistinctWrapper(T value, EqualComparator<T> equalComparator) {
        this.value = value;
        this.equalComparator = equalComparator;
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof DistinctWrapper) {
            obj = ((DistinctWrapper<?>) obj).value;
            if (ClassUtil._instanceof(obj, this.value.getClass())) {
                return equalComparator.apply((T) obj, this.value);
            }
        }
        return false;
    }


    @Override
    public int compare(T o1, T o2) {
        return 0;
    }
}

private static class DistinctSubscriber<T> extends IntermediateSubscriber<T, T> {

    private final EqualComparator<T> equalComparator;

    private final List<DistinctWrapper<T>> list = new ArrayList<>();

    private DistinctSubscriber(Subscriber<? super T> actualSubscriber, EqualComparator<T> equalComparator) {
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
