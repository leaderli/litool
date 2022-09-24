package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.util.ArrayList;
import java.util.List;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

/**
 * @author leaderli
 * @since 2022/9/24 2:50 PM
 */
public class TypeAdapters {

    private static final TypeAdapter<? super Integer> INTEGER = obj -> null;
    public static final TypeAdapterFactory INTEGER_FACTORY
            = newFactory(int.class, Integer.class, INTEGER);

    public static <TT> TypeAdapterFactory newFactory(
            final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        Lean lean = new Lean();
        List<TypeAdapterFactory> factories = new ArrayList<>();
        for (PrimitiveEnum value : PrimitiveEnum.PRIMITIVES) {

            TypeAdapter typeAdapter = (TypeAdapter<Object>) obj ->
                    StringConvert.parser(value.primitive, String.valueOf(obj))
                            .cast(Object.class)
                            .get(value.zero_value);
            factories.add(newFactory(LiTypeToken.get(value.primitive), typeAdapter));
            typeAdapter = (TypeAdapter<Object>) obj ->
                    StringConvert.parser(value.wrapper, String.valueOf(obj))
                            .cast(Object.class)
                            .get(value.zero_value);
            factories.add(newFactory(LiTypeToken.get(value.wrapper), typeAdapter));
        }
        for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {

            for (TypeAdapterFactory factory : factories) {
                TypeAdapter<?> typeAdapter = factory.create(lean, LiTypeToken.get(primitive.primitive));
                if (typeAdapter != null) {
                    print(primitive, typeAdapter.read(3));

                }
            }
        }


    }

    public static <TT> TypeAdapterFactory newFactory(final LiTypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
            @Override
            public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
                return typeToken.equals(type) ? (TypeAdapter<T>) typeAdapter : null;
            }
        };
    }

}
