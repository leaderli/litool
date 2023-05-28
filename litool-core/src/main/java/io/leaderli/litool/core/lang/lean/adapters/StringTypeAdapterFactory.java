package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.util.ObjectsUtil;

/**
 * @author leaderli
 * @since 2022/10/21 12:02 PM
 */
public class StringTypeAdapterFactory implements TypeAdapterFactory {
    final TypeAdapter<?> typeAdapter = new StringTypeAdapter();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        return ObjectsUtil.sameAny(type.getRawType(), String.class, CharSequence.class) ? (TypeAdapter<T>) typeAdapter : null;
    }

    static class StringTypeAdapter implements TypeAdapter<String> {
        @Override
        public String read(Object source, Lean lean) {
            return source != null ? String.valueOf(source) : null;
        }
    }
}
