package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.LeanFieldAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StrSubstitution;
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

        @SuppressWarnings("unchecked")
        public void populate(Object source, Object target) {
            Type declare = typeToken.getType();
            for (Field field : ReflectUtil.getFields(target.getClass())) {

                String key = lean.reflect_name_handlers.map(fu -> fu.apply(field)).first().get();
                Type targetType = TypeUtil.resolve(declare, field.getGenericType());

                Lino<LeanFieldAdapter> annotation = ReflectUtil.getAnnotation(field, LeanFieldAdapter.class);
                TypeAdapter<T> typeAdapter;
                if (annotation.present()) {
                    typeAdapter = (TypeAdapter<T>) annotation
                            .map(LeanFieldAdapter::value)
                            .assertTrue(cls -> {
                                ParameterizedTypeImpl adapterType = TypeUtil.resolve2Parameterized(cls, TypeAdapter.class);
                                if (adapterType.getActualTypeArguments()[0] == targetType) {
                                    return true;
                                }
                                throw new IllegalArgumentException(StrSubstitution.format("the {adapter} is not satisfied the field type {type}", adapterType, targetType));
                            })
                            .unzip(ReflectUtil::newInstance)
                            .assertNotNone()
                            .get();
                } else {
                    typeAdapter = lean.getAdapter(targetType);

                }
                BeanPath.simple(source, key)
                        .map(typeAdapter::read)
                        .ifPresent(v -> ReflectUtil.setFieldValue(target, field, v));
            }
        }

    }

}
