package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.PrimitiveEnum;

/**
 * @author leaderli
 * @since 2022/9/25
 */
public class PrimitiveTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    // 原始类型，没有泛型
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        PrimitiveEnum primitiveEnum = PrimitiveEnum.get(rawType);
        if (primitiveEnum == PrimitiveEnum.OBJECT) {
            return null;
        }
        return new PrimitiveTypeAdapter(primitiveEnum);
    }

    @SuppressWarnings("rawtypes")
    static class PrimitiveTypeAdapter implements TypeAdapter {
        private final PrimitiveEnum primitiveEnum;

        PrimitiveTypeAdapter(PrimitiveEnum primitiveEnum) {
            this.primitiveEnum = primitiveEnum;
        }

        @Override
        public Object read(Object source, Lean lean) {
            return primitiveEnum.read(source);
        }

        @Override
        public Object read(Lean lean) {
            return primitiveEnum.zero_value;
        }

    }

}
