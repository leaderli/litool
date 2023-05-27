package io.leaderli.litool.core.internal;

import io.leaderli.litool.core.type.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;


/**
 * 表示泛型数组类型的实现类。
 *
 * @see java.lang.reflect.GenericArrayType
 */
public final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
    private static final long serialVersionUID = 0;
    private final transient Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = TypeUtil.canonicalize(componentType);
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public int hashCode() {
        return componentType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GenericArrayType
                && TypeUtil.equals(this, (GenericArrayType) o);
    }

    @Override
    public String toString() {
        return TypeUtil.typeToString(componentType) + "[]";
    }
}
