package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CustomTypeAdapterFactory implements TypeAdapterFactory {

    private final Map<LiTypeToken<?>, TypeAdapter<?>> map = new LinkedHashMap<>();

    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        return get(typeToken);
    }


    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> put(LiTypeToken<T> token, TypeAdapter<T> typeAdapter) {
        return (TypeAdapter<T>) map.put(token, typeAdapter);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> get(LiTypeToken<T> token) {
        return (TypeAdapter<T>) map.get(token);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> computeIfAbsent(LiTypeToken<T> token, TypeAdapter<T> typeAdapter) {
        return (TypeAdapter<T>) map.computeIfAbsent(token, k -> typeAdapter);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> remove(LiTypeToken<T> token) {
        return (TypeAdapter<T>) map.remove(token);
    }

    public void clear() {
        map.clear();
    }

    public void forEach(BiConsumer<LiTypeToken<?>, TypeAdapter<?>> action) {
        map.forEach(action);
    }
}
