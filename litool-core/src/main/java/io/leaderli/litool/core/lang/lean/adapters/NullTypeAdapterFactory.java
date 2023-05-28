package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

/**
 * @author leaderli
 * @since 2022/10/21 12:02 PM
 */
public class NullTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        return (TypeAdapter<T>) new NullTypeAdapter();
    }

    static class NullTypeAdapter implements TypeAdapter<String> {
        @Override
        public String read(Object source, Lean lean) {
            return null;
        }
    }
}
