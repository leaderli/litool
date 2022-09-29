package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.lang.lean.*;
import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;


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
        return new ReflectAdapter<>(lean, type);
    }


    private static class ReflectAdapter<T> implements TypeAdapter<T> {

        private final Lean lean;
        private final LiTypeToken<T> typeToken;

        private ReflectAdapter(Lean lean, LiTypeToken<T> typeToken) {
            this.lean = lean;
            this.typeToken = typeToken;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(Object source) {
            TypeAdapter<T> adapter = this.lean.getAdapter(typeToken.getRawType());

            if (adapter instanceof ReflectAdapter) {
                return (T) ReflectUtil.newInstance(typeToken.getRawType())
                        .ifPresent(bean -> populate(source, bean))
                        .get();
            }
            return adapter.read(source);
        }

        public void populate(Object source, Object target) {

            for (Field field : ReflectUtil.getFields(target.getClass())) {
                Type targetType = TypeUtil.resolve(typeToken.getType(), field.getGenericType());
                performField(source, targetType, target, field);
            }
        }


        private void performField(Object source, Type targetType, Object target, Field field) {

            String key = lean.reflect_name_handlers.map(fu -> fu.apply(field)).first().get();


            TypeAdapter<T> typeAdapter = ReflectUtil.getAnnotation(field, LeanFieldAdapter.class)
                    .map(lf -> getLeanFieldTypeAdapter(targetType, lf))
                    .get(() -> lean.getAdapter(targetType));

            BeanPath.simple(source, key)
                    .map(fv -> typeAdapter.read(fv, lean))
                    .eitherSupplier(() -> {
                        if (typeAdapter instanceof NullableTypeAdapters) {
                            return ((NullableTypeAdapters<T>) typeAdapter).read(lean, source, targetType);
                        }
                        return null;
                    })
                    .map(Either::fold)
                    .ifPresent(v -> ReflectUtil.setFieldValue(target, field, v));
        }


        @SuppressWarnings("unchecked")
        private TypeAdapter<T> getLeanFieldTypeAdapter(Type targetType, LeanFieldAdapter annotation) {

            Class<? extends TypeAdapter<?>> cls = annotation.value();

            LiTuple2<TypeAdapter<?>, Type> find = computeIfAbsentHandler(cls);

            // the 2nd of tuple is TypeAdapter actualClassParameter, is always wrapper class .
            // add primitive support
            if (find._1 != null && !find._2.equals(targetType) && !find._2.equals(ClassUtil.primitiveToWrapper(TypeUtil.erase(targetType)))) {

                String msg = StrSubstitution.format("the {adapter} is not " + "satisfied the " + "field type {type}", cls, targetType);
                throw new IllegalArgumentException(msg);
            }
            return (TypeAdapter<T>) find._1;
        }

        @SuppressWarnings("unchecked")
        private LiTuple2<TypeAdapter<?>, Type> computeIfAbsentHandler(Class<? extends TypeAdapter<?>> cls) {

            LiTuple2<TypeAdapter<?>, Type> find = lean.reflect_value_handlers.get(cls);
            if (find != null) {
                return find;
            }
            Type actualTypeArgument = TypeUtil.resolve2Parameterized(cls, TypeAdapter.class).getActualTypeArguments()[0];
            find = ReflectUtil.newInstance(cls)
                    .tuple(adp -> actualTypeArgument)
                    .assertNotNone(() -> StrSubstitution.format("the {adapter} is cannot " + "create instance}", cls))
                    .cast(LiTuple2.class)
                    .get();
            synchronized (lean.reflect_value_handlers) {
                lean.reflect_value_handlers.put(cls, find);
            }
            return find;
        }


    }


}
