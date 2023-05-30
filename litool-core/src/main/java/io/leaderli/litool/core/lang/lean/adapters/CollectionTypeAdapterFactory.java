package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ObjectConstructor;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author leaderli
 * @since 2022/9/25 3:44 PM
 */
public class CollectionTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        if (!Collection.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }

        ObjectConstructor<T> constructor = lean.getConstructor(typeToken);
        if (constructor == null) {
            return null;
        }

        Type componentType = Lira.of(typeToken.getActualTypeArguments()).first().get(Object.class);
        return new CollectionAdapter(lean.getTypeAdapter(componentType), constructor);

    }

    public static final class CollectionAdapter<E> implements TypeAdapter<Iterable<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
        private final ObjectConstructor<Collection<E>> constructor;

        private CollectionAdapter(TypeAdapter<E> elementTypeAdapter, ObjectConstructor<Collection<E>> constructor) {
            this.elementTypeAdapter = elementTypeAdapter;
            this.constructor = constructor;
        }

        @Override
        public Iterable<E> read(Object source, Lean lean) {

            Collection<E> collection = constructor.get();

            Lira.iterableItr(source)
                    .map(e -> elementTypeAdapter.read(e, lean))
                    .nullable(() -> elementTypeAdapter.read(lean))
                    .forNullableEach(collection::add);

            return collection;

        }

        public Iterable<E> read(Lean lean) {

            return constructor.get();
        }
    }
}
