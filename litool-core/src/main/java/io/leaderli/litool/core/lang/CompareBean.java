package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.type.ClassUtil;

import java.util.Comparator;

/**
 * @author leaderli
 * @since 2022/9/2
 */
public class CompareBean<T> implements Comparator<T> {

    public final T value;
    public final EqualComparator<T> equalComparator;

    public CompareBean(T value, EqualComparator<T> equalComparator) {
        this.value = value;
        this.equalComparator = equalComparator;
    }


    @Override
    public int hashCode() {
        // only compare with equals
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof CompareBean) {
            obj = ((CompareBean<?>) obj).value;
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
