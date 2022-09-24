package io.leaderli.litool.core.type;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/25
 */
public final class ConstructorConstructor {

    private final Map<Type, InstanceCreator<?>> instanceCreators;

    public ConstructorConstructor() {
        this.instanceCreators = new HashMap<>();
    }

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }

    @SuppressWarnings("unchecked")
    public <T> Supplier<T> get(LiTypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();

        InstanceCreator<T> typeCreator = (InstanceCreator<T>) instanceCreators.get(type);
        if (typeCreator != null) {
            return () -> typeCreator.createInstance(type);
        }

        return () -> (T) ReflectUtil.newInstance(rawType).get();
    }
}
