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
    private final Type ownerType;
    private final Class<?> rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
        if (rawType instanceof Class<?>) {
            Class<?> rawTypeAsClass = (Class<?>) rawType;
            boolean isStaticOrTopLevelClass =
                    Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null;
            LiAssertUtil.assertTrue(ownerType != null || isStaticOrTopLevelClass);
        }

        this.ownerType = ownerType == null ? null : TypeUtil.canonicalize(ownerType);
        this.rawType = TypeUtil.erase(TypeUtil.canonicalize(rawType));
        this.typeArguments = typeArguments.length == 0 ? TypeUtil.EMPTY_TYPE_ARRAY : typeArguments.clone();

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
        return make(TypeUtil.erase(type));
    }

    public static ParameterizedTypeImpl make(ParameterizedType parameterizedType) {
        return make(parameterizedType.getOwnerType(), (Class<?>) parameterizedType.getRawType(),
                parameterizedType.getActualTypeArguments());
    }

    public static ParameterizedTypeImpl make(Class<?> rawType) {
        return new ParameterizedTypeImpl(rawType.getEnclosingClass(), rawType, rawType.getTypeParameters());
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
        return typeArguments == TypeUtil.EMPTY_TYPE_ARRAY ? TypeUtil.EMPTY_TYPE_ARRAY : typeArguments.clone();
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
        StringBuilder sb = new StringBuilder();

        if (ownerType != null) {
            if (ownerType instanceof Class) {
                sb.append(((Class<?>) ownerType).getName());
            } else {
                sb.append(ownerType);
            }

            sb.append("$");

            if (ownerType instanceof ParameterizedTypeImpl) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.getName().replace(((ParameterizedTypeImpl) ownerType).rawType.getName() + "$", ""));
            } else {
                sb.append(rawType.getSimpleName());
            }
        } else {

            sb.append(rawType.getName());
        }

        if (typeArguments != null && typeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t : typeArguments) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(t.getTypeName());
                first = false;
            }
            sb.append(">");
        }

        return sb.toString();


    }
}
