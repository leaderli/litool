package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.lang.lean.TypeAdapterFactory;
import io.leaderli.litool.core.type.LiTypeToken;

/**
 * @author leaderli
 * @since 2022/10/21 12:02 PM
 * 非严格模式下的默认兜底适配器工厂类，总是转换为null
 */
public class NullTypeAdapterFactory implements TypeAdapterFactory {

    private static final TypeAdapter<Object> SHARE = new NullTypeAdapter();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
        return (TypeAdapter<T>) SHARE;
    }

    private static class NullTypeAdapter implements TypeAdapter<Object> {
        @Override
        public Object read(Object source, Lean lean) {
            return null;
        }
    }
}
