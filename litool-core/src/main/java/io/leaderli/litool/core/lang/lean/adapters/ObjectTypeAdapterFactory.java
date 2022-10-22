package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/10/21 12:02 PM
 */
public class ObjectTypeAdapterFactory implements TypeAdapterFactory {
    final TypeAdapter<?> typeAdapter = new ObjectTypeAdapter();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        return type.getRawType() == Object.class ? (TypeAdapter<T>) typeAdapter : null;
    }

    static class ObjectTypeAdapter implements TypeAdapter<Object> {
        @Override
        public Object read(Object source, Lean lean) {
            if (source != null) {

                TypeAdapter<Object> adapter = lean.getAdapter(source.getClass());
                if (adapter instanceof MapTypeAdapterFactory.MapAdapter || adapter instanceof ReflectAdapterFactory.ReflectAdapter) {
                    return lean.fromBean(source, Map.class);
                } else if (adapter instanceof CollectionTypeAdapterFactory.CollectionAdapter || adapter instanceof ArrayTypeAdapterFactory.ArrayAdapter) {
                    return lean.fromBean(source, List.class);
                }

            }
            return source;
        }
    }
}
