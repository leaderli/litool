package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/9/25 3:44 PM
 */
public class ArrayTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {

        Type type = typeToken.getType();
        if (!(type instanceof GenericArrayType)) {
            return null;
        }
        type = ((GenericArrayType) type).getGenericComponentType();

        return new ArrayAdapter(TypeUtil.erase(type), lean.getTypeAdapter(type));

    }

    public static final class ArrayAdapter<E> implements TypeAdapter<E[]> {
        private final Class<E> componentType;
        private final TypeAdapter<E> elementTypeAdapter;

        private ArrayAdapter(Class<E> componentType, TypeAdapter<E> elementTypeAdapter) {
            this.componentType = componentType;
            this.elementTypeAdapter = elementTypeAdapter;
        }

        @Override
        public E[] read(Object source, Lean lean) {

            return Lira.iterableItr(source).map(e -> elementTypeAdapter.read(e, lean)).toNullableArray(componentType);
        }
    }

}
