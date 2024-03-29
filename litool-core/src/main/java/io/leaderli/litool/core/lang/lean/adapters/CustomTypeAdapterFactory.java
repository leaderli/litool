package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTypeAdapterFactory implements TypeAdapterFactory {

    private final Map<LiTypeToken<?>, TypeAdapter<?>> map = new LinkedHashMap<>();

    public CustomTypeAdapterFactory() {
        puts(new DateTypeAdapter(), Date.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        return (TypeAdapter<T>) map.get(typeToken);
    }


    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> put(LiTypeToken<T> token, TypeAdapter<T> typeAdapter) {
        return (TypeAdapter<T>) map.put(token, typeAdapter);
    }

    @SafeVarargs
    public final <T> void puts(TypeAdapter<T> typeAdapter, Class<? super T>... classes) {
        for (Class<? super T> cls : classes) {
            put(LiTypeToken.ofType(cls), typeAdapter);
        }
    }
}
