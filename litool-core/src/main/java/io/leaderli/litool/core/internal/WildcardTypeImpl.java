package io.leaderli.litool.core.internal;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

/**
 * WildcardTypeImpl 类实现了 WildcardType 接口，支持多个上界和多个下界。我们只支持 Java 6 需要的 - 最多一个边界。
 * 如果设置了下界，则上界必须是 Object.class。
 */
public final class WildcardTypeImpl implements WildcardType, Serializable {
    private final transient Type upperBound;
    private final transient Type lowerBound;

    /**
     * 构造一个 WildcardTypeImpl 对象。
     *
     * @param upperBounds 上界类型数组，只能有一个元素
     * @param lowerBounds 下界类型数组，最多只能有一个元素
     */
    public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
        LiAssertUtil.assertTrue(lowerBounds.length <= 1);
        LiAssertUtil.assertTrue(upperBounds.length == 1);

        if (lowerBounds.length == 1) {
            Objects.requireNonNull(lowerBounds[0]);
            TypeUtil.checkNotPrimitive(lowerBounds[0]);
            LiAssertUtil.assertTrue(upperBounds[0] == Object.class);
            this.lowerBound = TypeUtil.canonicalize(lowerBounds[0]);
            this.upperBound = Object.class;

        } else {
            Objects.requireNonNull(upperBounds[0]);
            TypeUtil.checkNotPrimitive(upperBounds[0]);
            this.lowerBound = null;
            this.upperBound = TypeUtil.canonicalize(upperBounds[0]);
        }
    }

    @Override
    public Type[] getUpperBounds() {
        return new Type[]{upperBound};
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBound != null ? new Type[]{lowerBound} : TypeUtil.EMPTY_TYPE_ARRAY;
    }

    @Override
    public int hashCode() {
        return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
                ^ (31 + upperBound.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof WildcardType
                && TypeUtil.equals(this, (WildcardType) other);
    }

    @Override
    public String toString() {
        if (lowerBound != null) {
            return "? super " + TypeUtil.typeToString(lowerBound);
        } else if (upperBound == Object.class) {
            return "?";
        } else {
            return "? extends " + TypeUtil.typeToString(upperBound);
        }
    }
}
