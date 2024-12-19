package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Array;
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
        Class<?> rawType = TypeUtil.erase(type);
        if (rawType.isPrimitive()) {
            return new PrimitiveArrayAdapter(rawType);
        }
        return new ArrayAdapter(rawType, lean.getTypeAdapter(type));

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    // 原始类型，没有泛型
    public static final class PrimitiveArrayAdapter implements TypeAdapter {
        private final Class componentType;
        private final PrimitiveTypeAdapterFactory.PrimitiveTypeAdapter elementTypeAdapter;

        public PrimitiveArrayAdapter(Class componentType) {
            this.componentType = componentType;
            this.elementTypeAdapter = new PrimitiveTypeAdapterFactory.PrimitiveTypeAdapter(PrimitiveEnum.get(componentType));
        }

        @Override
        public Object read(Object source, Lean lean) {

            Object[] arr = Lira.iterableItr(source)
                    .map(e -> elementTypeAdapter.read(e, lean))
                    .nullable(() -> elementTypeAdapter.read(lean))
                    .toNullableArray(componentType);
            Object copy = Array.newInstance(componentType, arr.length);
            for (int i = 0; i < arr.length; i++) {
                Array.set(copy, i, arr[i]);
            }
            return copy;
        }


        @Override
        public Object read(Lean lean) {
            return Array.newInstance(componentType, 0);
        }
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

            return Lira.iterableItr(source)
                    .map(e -> elementTypeAdapter.read(e, lean))
                    .nullable(() -> elementTypeAdapter.read(lean))
                    .toNullableArray(componentType);
        }

        @Override
        public E[] read(Lean lean) {

            return ClassUtil.newWrapperArray(componentType, 0);
        }
    }

}
