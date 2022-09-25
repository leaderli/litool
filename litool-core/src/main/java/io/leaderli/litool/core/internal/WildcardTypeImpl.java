package io.leaderli.litool.core.internal;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

/**
 * The WildcardType interface supports multiple upper bounds and multiple
 * lower bounds. We only support what the Java 6 language needs - at most one
 * bound. If a lower bound is set, the upper bound must be Object.class.
 */
public final class WildcardTypeImpl implements WildcardType, Serializable {
    private static final long serialVersionUID = 0;
    private final Type upperBound;
    private final Type lowerBound;

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
        return lowerBound != null ? new Type[]{lowerBound} : ParameterizedTypeImpl.EMPTY_TYPE_ARRAY;
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
