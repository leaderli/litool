package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * a decorator provide a custom compare function, commonly used to {@link  java.util.Set}
 *
 * @author leaderli
 * @since 2022/9/2
 */
public class CompareDecorator<T> {

    public final T value;
    public final boolean allowNull;
    public final EqualComparator<? super T> equalComparator;

    public CompareDecorator(T value, EqualComparator<? super T> equalComparator) {
        this(true, value, equalComparator);
    }

    public CompareDecorator(boolean allowNull, T value, EqualComparator<? super T> equalComparator) {
        this.allowNull = allowNull;
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


        if (obj instanceof CompareDecorator) {
            Object objValue = ((CompareDecorator<?>) obj).value;
            if (this.value == null) {
                return allowNull && objValue == null;
            }
            if (ClassUtil._instanceof(objValue, this.value.getClass())) {
                return equalComparator.apply((T) objValue, this.value);
            }
        }
        return false;
    }


}
