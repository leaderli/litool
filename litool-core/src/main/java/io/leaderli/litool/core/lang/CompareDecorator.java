package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * 用于提供自定义比较函数的装饰器，通常用于 {@link java.util.Set}
 *
 * @param <T> 要比较的对象类型
 * @since 2022/9/2
 */
public class CompareDecorator<T> {

    public final T value;
    public final boolean allowNull;
    public final EqualComparator<? super T> equalComparator;

    /**
     * 创建一个装饰器，允许空值
     *
     * @param value           要比较的对象
     * @param equalComparator 自定义比较器
     * @see #CompareDecorator(boolean, Object, EqualComparator)
     */
    public CompareDecorator(T value, EqualComparator<? super T> equalComparator) {
        this(true, value, equalComparator);
    }

    /**
     * 创建一个装饰器
     *
     * @param allowNull       是否允许空值
     * @param value           要比较的对象
     * @param equalComparator 自定义比较器
     */
    public CompareDecorator(boolean allowNull, T value, EqualComparator<? super T> equalComparator) {
        this.allowNull = allowNull;
        this.value = value;
        this.equalComparator = equalComparator;
    }

    /**
     * 重写 hashCode 方法，其值恒返回0，使其只能用于和 equals 方法比较
     */
    @Override
    public int hashCode() {
        // only compare with equals
        return 0;
    }

    /**
     * 重写 equals 方法，使用自定义比较器进行比较
     *
     * @param obj 要比较的对象
     * @return 是否相等
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {


        if (obj instanceof CompareDecorator) {
            Object objValue = ((CompareDecorator<?>) obj).value;
            if (this.value == null) {
                return allowNull && objValue == null;
            }
            if (ClassUtil.isInstanceof(objValue, this.value.getClass())) {
                return equalComparator.apply((T) objValue, this.value);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "compare(" + value + ")";
    }
}
