package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.lean.adapters.*;
import io.leaderli.litool.core.type.LiTypeToken;

/**
 * @author leaderli
 * @since 2022/9/24 2:50 PM
 */
public class TypeAdapters {

    public static final TypeAdapterFactory PRIMITIVE_FACTORY = new PrimitiveTypeAdapter();
    public static final TypeAdapterFactory STRING_FACTORY = new StringTypeAdapterFactory();
    public static final TypeAdapterFactory OBJECT_FACTORY = new ObjectTypeAdapterFactory();

    public static final TypeAdapterFactory ITERABLE_FACTORY = new CollectionTypeAdapterFactory();
    public static final TypeAdapterFactory ARRAY_FACTORY = new ArrayTypeAdapterFactory();
    public static final TypeAdapterFactory MAP_FACTORY = new MapTypeAdapterFactory();
    public static final TypeAdapterFactory REFLECT_FACTORY = new ReflectAdapterFactory();


    public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
            @Override
            public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
                return typeToken.getRawType() == type ? (TypeAdapter<T>) typeAdapter : null;
            }

        };
    }


}
