package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author leaderli
 * @since 2022/9/25 3:44 PM
 */
class CollectionTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        if (!Iterable.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        Type type1 = type.getType();
        if (lean.get(type) == null) {
            return null;
        }

        Type componentType = Lira.of(type.getActualTypeArguments()).first().get(Object.class);
        return new Adapter(lean.getAdapter(LiTypeToken.of(componentType)));

    }

    private static final class Adapter<E> implements TypeAdapter<Iterable<E>> {
        private final TypeAdapter<E> elementTypeAdapter;

        private Adapter(TypeAdapter<E> elementTypeAdapter) {
            this.elementTypeAdapter = elementTypeAdapter;
        }

        @Override
        public Iterable<E> read(Object obj) {

            Collection<E> collection = new ArrayList<>();
            if (obj instanceof Iterable) {

                ((Iterable<?>) obj).forEach(e -> collection.add(elementTypeAdapter.read(e)));
            }

            return collection;
        }
    }
}
