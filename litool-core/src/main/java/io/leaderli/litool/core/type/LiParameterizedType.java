package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.StringJoiner;

/**
 * @author leaderli
 * @since 2022/9/21
 */
public class LiParameterizedType implements ParameterizedType {
    private final Class<?> rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public LiParameterizedType(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    public static LiParameterizedType make(Class<?> cls) {
        return new LiParameterizedType(cls, cls.getTypeParameters(), null);
    }

    public static LiParameterizedType make(ParameterizedType parameterizedType) {
        return make((Class<?>) parameterizedType.getRawType(), parameterizedType.getOwnerType(), parameterizedType.getActualTypeArguments());
    }

    public static LiParameterizedType make(Class<?> cls, Type ownerType, Type... actualTypeArguments) {
        return new LiParameterizedType(cls, actualTypeArguments, ownerType);
    }


    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @SuppressWarnings("rawtypes")
    public Class[] getActualClassArguments() {
        return Lira.of(actualTypeArguments).map(TypeUtil::erase).toArray(Class.class);
    }

    @SuppressWarnings("rawtypes")
    public Lino<Class> getActualClassArgument() {
        return getActualClassArgument(0);
    }

    @SuppressWarnings("rawtypes")
    public Lino<Class> getActualClassArgument(int index) {
        return Lira.of(actualTypeArguments).get(index).map(TypeUtil::erase);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (ownerType != null) {
            sb.append(ownerType.getTypeName());

            sb.append("$");

            if (ownerType instanceof LiParameterizedType) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.getName().replace(((LiParameterizedType) ownerType).rawType.getName() + "$",
                        ""));
            } else
                sb.append(rawType.getSimpleName());
        } else
            sb.append(rawType.getName());

        if (actualTypeArguments != null) {
            StringJoiner sj = new StringJoiner(", ", "<", ">");
            sj.setEmptyValue("");
            for (Type t : actualTypeArguments) {
                sj.add(t.getTypeName());
            }
            sb.append(sj);
        }

        return sb.toString();
    }
}
