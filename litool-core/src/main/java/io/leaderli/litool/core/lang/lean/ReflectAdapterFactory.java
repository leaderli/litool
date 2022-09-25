package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/25
 */
public class ReflectAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        // use the cache, to avoid stackoverflow should not use getAdapter
        TypeAdapter<T> adapter = lean.getCacheAdapter(type);
        if (adapter != null) {
            return adapter;
        }
        return new Adapter<>(lean, type);
    }

    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type, Supplier<TypeAdapter<T>> supplier) {
        TypeAdapter<T> adapter = lean.getAdapter(type);
        if (adapter != null) {
            return adapter;
        }
        return new Adapter<>(lean, type);
    }

    private static class Adapter<T> implements TypeAdapter<T> {

        private final Lean lean;
        private final LiTypeToken<T> type;

        private Adapter(Lean lean, LiTypeToken<T> type) {
            this.lean = lean;
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(Object obj) {
            TypeAdapter<T> adapter = lean.getAdapter(type.getRawType());

            if (adapter instanceof Adapter) {
                return (T) ReflectUtil.newInstance(type.getRawType())
                        .ifPresent(bean -> lean.populate(type.getType(), bean, obj))
                        .get();
            }
            return adapter.read(obj);
        }
    }

}
