package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ObjectConstructor;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
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
        Type[] componentType = Lira.<TypeVariable>of(type.getRawType().getTypeParameters()).toArray(Type.class);
        Type keyType = componentType[0];
        if (keyType == String.class) {
            return new StringAdapter(lean.getAdapter(String.class), lean.getAdapter(componentType[1]), constructor);
        }
        return new Adapter(lean.getAdapter(LiTypeToken.of(keyType)), lean.getAdapter(componentType[1]), constructor);

    }

    private static final class StringAdapter<V> implements TypeAdapter<Map<String, V>> {
        private final TypeAdapter<String> keyTypeAdapter;

        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<Map<String, V>> constructor;

        private StringAdapter(TypeAdapter<String> keyTypeAdapter, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<Map<String, V>> constructor) {
            this.keyTypeAdapter = keyTypeAdapter;
            this.valueTypeAdapter = valueTypeAdapter;
            this.constructor = constructor;
        }

        @Override
        public Map<String, V> read(Object source) {
            if (source == null) {
                return null;
            }

            Map<String, V> map = constructor.get();
            if (source instanceof Map) {

                ((Map<?, ?>) source).forEach((k, v) -> {
                    String rk = keyTypeAdapter.read(k);
                    V rv = valueTypeAdapter.read(v);
                    map.put(rk, rv);
                });
            } else if (PrimitiveEnum.get(source) == PrimitiveEnum.OBJECT) {

                for (Field field : ReflectUtil.getFields(source.getClass())) {
                    String rk = field.getName();
                    Object v = ReflectUtil.getFieldValue(source, field).get();
                    V rv = valueTypeAdapter.read(v);
                    map.put(rk, rv);
                }
            }

            return map;
        }
    }

    private static final class Adapter<K, V> implements TypeAdapter<Map<K, V>> {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<Map<K, V>> constructor;

        private Adapter(TypeAdapter<K> keyTypeAdapter, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<Map<K, V>> constructor) {
            this.keyTypeAdapter = keyTypeAdapter;
            this.valueTypeAdapter = valueTypeAdapter;
            this.constructor = constructor;
        }

        @Override
        public Map<K, V> read(Object source) {

            Map<K, V> map = constructor.get();
            if (source instanceof Map) {

                ((Map<?, ?>) source).forEach((k, v) -> {
                    K rk = keyTypeAdapter.read(k);
                    V rv = valueTypeAdapter.read(v);
                    map.put(rk, rv);
                });
            }


            return map;
        }
    }
}
