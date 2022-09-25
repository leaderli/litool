package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.core.lang.lean.adapters.CollectionTypeAdapterFactory;
import io.leaderli.litool.core.lang.lean.adapters.MapTypeAdapterFactory;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/9/24 2:50 PM
 */
public class TypeAdapters {

    public static final ImmutableList<TypeAdapterFactory> PRIMITIVE_FACTORIES = init();

    static ImmutableList<TypeAdapterFactory> init() {
        List<TypeAdapterFactory> factories = new ArrayList<>();
        for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {

            factories.add(new TypeAdapterFactory() {
                @SuppressWarnings("unchecked")
                @Override
                public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
                    Class<? super T> rawType = typeToken.getRawType();

                    if (rawType == primitive.primitive || rawType == primitive.wrapper) {

                        return obj -> {
                            if (obj.getClass() == primitive.wrapper) {
                                return (T) obj;
                            } else if (obj instanceof String) {
                                return (T) StringConvert.parser(primitive.wrapper, (String) obj).assertNotNone(obj + " cannot convert to int").get();
                            } else if (obj instanceof Number) {
                                return (T) ClassUtil.castDouble(((Number) obj).doubleValue(), primitive);
                            }
                            throw new IllegalArgumentException(obj + " cannot convert to int");

                        };
                    }

                    return null;
                }
            });
        }
        return ImmutableList.of(factories);
    }

    public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, obj -> obj == null ? null : String.valueOf(obj));
    public static final TypeAdapterFactory MAP_FACTORY = new MapTypeAdapterFactory();
    public static final TypeAdapterFactory ITERABLE_FACTORY = new CollectionTypeAdapterFactory();
    public static final TypeAdapterFactory OBJECT_FACTORY = newFactory(Object.class, obj -> obj);
    public static final TypeAdapterFactory REFLECT_FACTORY = new ReflectAdapterFactory();


    public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == unboxed || rawType == boxed) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + boxed.getName()
                        + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

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
