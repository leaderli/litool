package io.leaderli.litool.core.internal;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.TypeUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/9/21
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
        if (rawType instanceof Class<?>) {
            Class<?> rawTypeAsClass = (Class<?>) rawType;
            boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers())
                    || rawTypeAsClass.getEnclosingClass() == null;
            LiAssertUtil.assertTrue(ownerType != null || isStaticOrTopLevelClass);
        }

        this.ownerType = ownerType == null ? null : TypeUtil.canonicalize(ownerType);
        this.rawType = TypeUtil.canonicalize(rawType);
        this.typeArguments = typeArguments.length == 0 ? EMPTY_TYPE_ARRAY : typeArguments.clone();

        for (int t = 0, length = this.typeArguments.length; t < length; t++) {
            Objects.requireNonNull(this.typeArguments[t]);
            TypeUtil.checkNotPrimitive(this.typeArguments[t]);
            this.typeArguments[t] = TypeUtil.canonicalize(this.typeArguments[t]);
        }
    }

    public static ParameterizedTypeImpl make(Type type) {

        if (type instanceof ParameterizedType) {
            return make((ParameterizedType) type);
        }
        return make(null, TypeUtil.erase(type));
    }

    public static ParameterizedTypeImpl make(Class<?> rawType) {
        return new ParameterizedTypeImpl(null, rawType, rawType.getTypeParameters());
    }

    public static ParameterizedTypeImpl make(ParameterizedType parameterizedType) {
        return make(parameterizedType.getOwnerType(), (Class<?>) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
    }

    public static ParameterizedTypeImpl make(Type ownerType, Class<?> rawType, Type... actualTypeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, actualTypeArguments);
    }

    public Class<?>[] getActualClassArguments() {
        return Lira.of(typeArguments).map(TypeUtil::erase)
                .cast(Class.class)
                .toArray(Class.class);
    }

    public Lino<Class<?>> getActualClassArgument() {
        return getActualClassArgument(0);
    }

    public Lino<Class<?>> getActualClassArgument(int index) {
        return Lira.of(typeArguments).get(index).map(TypeUtil::erase);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return typeArguments == EMPTY_TYPE_ARRAY ? EMPTY_TYPE_ARRAY : typeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(typeArguments)
                ^ rawType.hashCode()
                ^ ObjectsUtil.hashCodeOrZero(ownerType);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ParameterizedType
                && TypeUtil.equals(this, (ParameterizedType) other);
    }

    @Override
    public String toString() {
        int length = typeArguments.length;
        if (length == 0) {
            return TypeUtil.typeToString(rawType);
        }

        StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
        stringBuilder.append(TypeUtil.typeToString(rawType)).append("<").append(TypeUtil.typeToString(typeArguments[0]));
        for (int i = 1; i < length; i++) {
            stringBuilder.append(", ").append(TypeUtil.typeToString(typeArguments[i]));
        }
        return stringBuilder.append(">").toString();
    }
}
