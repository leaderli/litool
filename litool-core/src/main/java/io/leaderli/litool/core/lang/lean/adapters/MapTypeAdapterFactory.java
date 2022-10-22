package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ObjectConstructor;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/9/25
 */
public class MapTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        if (!Map.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        // check objectConstructor exist
        ObjectConstructor<T> constructor = lean.getConstructor(type);
        if (constructor == null) {
            return null;
        }
        Type[] componentType = type.getActualTypeArguments();
        Type keyType = componentType[0];
        Type valueType = componentType[1];
        TypeAdapter keyTypeAdapter = lean.getAdapter(LiTypeToken.of(keyType));
        TypeAdapter valueTypeAdapter = lean.getAdapter(LiTypeToken.of(valueType));
        return new MapAdapter(keyTypeAdapter, valueTypeAdapter, constructor);

    }


    public static final class MapAdapter<K, V> implements TypeAdapter<Map<K, V>> {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<Map<K, V>> constructor;

        private MapAdapter(TypeAdapter<K> keyTypeAdapter, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<Map<K,
                V>> constructor) {
            this.keyTypeAdapter = keyTypeAdapter;
            this.valueTypeAdapter = valueTypeAdapter;
            this.constructor = constructor;
        }


        @Override
        public Map<K, V> read(Object source, Lean lean) {

            Map<K, V> map = constructor.get();

            if (source instanceof Map) {

                ((Map<?, ?>) source).forEach((k, v) -> handle(lean, map, k, v));
            } else if (PrimitiveEnum.get(source) == PrimitiveEnum.OBJECT) {
                for (Field field : ReflectUtil.getFields(source.getClass())) {
                    Object v = ReflectUtil.getFieldValue(source, field).get();
                    handle(lean, map, field.getName(), v);
                }
            }


            return map;
        }

        public void handle(Lean lean, Map<K, V> map, Object k, Object v) {
            K rk = keyTypeAdapter.read(k, lean);
            V rv = valueTypeAdapter.read(v, lean);
            if (rk != null) {
                map.put(rk, rv);
            }
        }

    }
}
