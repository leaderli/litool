package io.leaderli.litool.core.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.StringJoiner;

/**
 * @author leaderli
 * @since 2022/9/21
 */
public class LiParameterizedType<T> implements ParameterizedType {
    private final Class<T> rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public LiParameterizedType(Class<T> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    public static <T> LiParameterizedType<T> make(Class<T> cls) {

        return new LiParameterizedType<>(cls, cls.getTypeParameters(), null);
    }

    public static <T> LiParameterizedType<T> make(Class<T> cls, Class<?>... actualTypeArguments) {
        return new LiParameterizedType<>(cls, actualTypeArguments, null);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
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
                sb.append(rawType.getName().replace(((LiParameterizedType<?>) ownerType).rawType.getName() + "$",
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
