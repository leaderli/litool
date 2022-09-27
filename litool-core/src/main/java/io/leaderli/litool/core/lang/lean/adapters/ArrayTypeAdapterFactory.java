package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

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

        return new ArrayAdapter(TypeUtil.erase(type), lean.getAdapter(type));

    }

    private static final class ArrayAdapter<E> implements TypeAdapter<E[]> {
        private final Class<E> componentType;
        private final TypeAdapter<E> elementTypeAdapter;

        private ArrayAdapter(Class<E> componentType, TypeAdapter<E> elementTypeAdapter) {
            this.componentType = componentType;
            this.elementTypeAdapter = elementTypeAdapter;
        }

        @Override
        public E[] read(Object source) {

            if (source == null) {
                return null;
            }
            Collection<E> collection = new ArrayList<>();
            if (source instanceof Iterable) {
                ((Iterable<?>) source).forEach(e -> collection.add(elementTypeAdapter.read(e)));
            } else if (source.getClass().isArray()) {
                E[] objects = CollectionUtils.toWrapperArray(source);
                for (E e : objects) {
                    collection.add(elementTypeAdapter.read(e));
                }
            }

            return Lira.of(collection).toArray(componentType);
        }
    }
}
