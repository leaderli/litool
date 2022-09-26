package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
        private final LiTypeToken<T> typeToken;

        private Adapter(Lean lean, LiTypeToken<T> typeToken) {
            this.lean = lean;
            this.typeToken = typeToken;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(Object source) {
            TypeAdapter<T> adapter = lean.getAdapter(typeToken.getRawType());

            if (adapter instanceof Adapter) {
                return (T) ReflectUtil.newInstance(typeToken.getRawType())
                        .ifPresent(bean -> populate(source, bean))
                        .get();
            }
            return adapter.read(source);
        }

        public void populate(Object source, Object target) {
            Type declare = typeToken.getType();
            for (Field field : ReflectUtil.getFields(target.getClass())) {
//                ReflectUtil.findAnnotationsWithMetaAnnotation(field,)
                BeanPath.simple(source, field.getName())
                        .map(value -> lean.fromBean(value, TypeUtil.resolve(declare, field.getGenericType())))
                        .ifPresent(v -> ReflectUtil.setFieldValue(target, field, v));
            }
        }

    }

}
