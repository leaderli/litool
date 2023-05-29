package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/10/21 12:02 PM
 */
public class EnumTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        Class<? super T> raw = typeToken.getRawType();
        if (!raw.isEnum()) {
            return null;
        }


        Enum[] enums = (Enum[]) ReflectUtil.getMethod(raw, "values").unzip(m -> ReflectUtil.invokeMethod(m, raw)).get();

        Map<Object, Enum> map = new HashMap<>();
        for (Enum e : enums) {
            map.put(e, e);
            map.put(e.name(), e);
            map.put(e.ordinal(), e);
        }

        return new EnumTypeAdapter(map);
    }

    static class EnumTypeAdapter<T> implements TypeAdapter<T> {

        private final Map<Object, Enum<?>> map;

        public EnumTypeAdapter(Map<Object, Enum<?>> map) {
            this.map = map;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(Object source, Lean lean) {
            return (T) map.get(source);
        }
    }
}
