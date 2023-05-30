package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.LeanValue;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;


/**
 * @author leaderli
 * @since 2022/9/25
 */
public class ReflectTypeAdapterFactory implements TypeAdapterFactory {


    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {

        Class<? super T> raw = typeToken.getRawType();
        if (ModifierUtil.isAbstract(raw) || ReflectUtil.newInstance(typeToken.getRawType()).absent()) {
            return null;
        }

        return new ReflectAdapter<>(lean, typeToken);
    }


    public static class ReflectAdapter<T> implements TypeAdapter<T> {
        private final LiTypeToken<T> typeToken;

        public ReflectAdapter(Lean lean, LiTypeToken<T> typeToken) {
            this.typeToken = typeToken;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(Object source, Lean lean) {
            TypeAdapter<T> adapter = lean.getTypeAdapter(typeToken.getRawType());
            T bean = (T) ReflectUtil.newInstance(typeToken.getRawType()).get();
            populate(source, bean, lean);
            return bean;
        }

        public void populate(Object source, Object target, Lean lean) {

            for (Field field : ReflectUtil.getFields(target.getClass()).filter(f -> !ModifierUtil.isStatic(f))) {
                Type targetType = TypeUtil.resolve(typeToken.getType(), field.getGenericType());
                performField(source, targetType, target, field, lean);
            }
        }


        private void performField(Object source, Type targetType, Object target, Field field, Lean lean) {

            String key = lean.leanKeyHandlers.map(fu -> fu.apply(field)).first().get();


            TypeAdapter<T> typeAdapter = ReflectUtil.getAnnotation(field, LeanValue.class)
                    .map(lf -> getLeanFieldTypeAdapter(targetType, lf, lean))
                    .get(() -> lean.getTypeAdapter(targetType));

            BeanPath.simple(source, key)
                    .map(fv -> typeAdapter.read(fv, lean))
                    .or(() -> typeAdapter.read(lean))
                    .ifPresent(v -> ReflectUtil.setFieldValue(target, field, v));
        }


        @SuppressWarnings("unchecked")
        private TypeAdapter<T> getLeanFieldTypeAdapter(Type targetType, LeanValue annotation, Lean lean) {

            Class<? extends TypeAdapter<?>> cls = annotation.value();

            LiTuple2<TypeAdapter<?>, Type> find = computeIfAbsentHandler(cls, lean);

            // the 2nd of tuple is TypeAdapter actualClassParameter, is always wrapper class .
            // add primitive support
            if (find._1 != null && !find._2.equals(targetType) && !find._2.equals(ClassUtil.primitiveToWrapper(TypeUtil.erase(targetType)))) {

                String msg = StrSubstitution.format("the {adapter} is not " + "satisfied the " + "field type {type}", cls, targetType);
                throw new IllegalArgumentException(msg);
            }
            return (TypeAdapter<T>) find._1;
        }

        @SuppressWarnings("unchecked")
        private LiTuple2<TypeAdapter<?>, Type> computeIfAbsentHandler(Class<? extends TypeAdapter<?>> cls, Lean lean) {

            LiTuple2<TypeAdapter<?>, Type> find = lean.leanValueHandlers.get(cls);
            if (find != null) {
                return find;
            }
            Type actualTypeArgument = TypeUtil.resolve2Parameterized(cls, TypeAdapter.class).getActualTypeArguments()[0];
            find = ReflectUtil.newInstance(cls)
                    .tuple2(actualTypeArgument)
                    .assertNotNone(() -> StrSubstitution.format("the {adapter} is cannot " + "create instance}", cls))
                    .cast(LiTuple2.class)
                    .get();
            synchronized (lean.leanValueHandlers) {
                lean.leanValueHandlers.put(cls, find);
            }
            return find;
        }


    }


}
