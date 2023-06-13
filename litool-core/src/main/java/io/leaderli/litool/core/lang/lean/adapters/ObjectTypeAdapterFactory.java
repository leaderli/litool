package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * 当转换的目标类型为 Object 时，我们根据原始数据的实际类型，挑选一个最合适的目标类型去转换
 * 支持的类有
 * <ul>
 *     <li>
 *
 *     </li>
 * </ul>
 * @see MapTypeAdapterFactory.MapAdapter
 * @see ReflectTypeAdapterFactory.ReflectAdapter
 * @see CollectionTypeAdapterFactory.CollectionAdapter
 * @see ArrayTypeAdapterFactory.ArrayAdapter
 * @since 2022/10/21 12:02 PM
 */
public class ObjectTypeAdapterFactory implements TypeAdapterFactory {
    final TypeAdapter<?> typeAdapter = new ObjectTypeAdapter();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> typeToken) {
        return typeToken.getRawType() == Object.class ? (TypeAdapter<T>) typeAdapter : null;
    }

    static class ObjectTypeAdapter implements TypeAdapter<Object> {
        @Override
        public Object read(Object source, Lean lean) {

            TypeAdapter<Object> adapter = lean.getTypeAdapter(source.getClass());
            if (adapter instanceof MapTypeAdapterFactory.MapAdapter || adapter instanceof ReflectTypeAdapterFactory.ReflectAdapter) {
                return lean.fromBean(source, Map.class);
            } else if (adapter instanceof CollectionTypeAdapterFactory.CollectionAdapter || adapter instanceof ArrayTypeAdapterFactory.ArrayAdapter) {
                return lean.fromBean(source, List.class);
            }

            return source;
        }
    }
}
