package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Type;
import java.util.*;

/**
 * a collection that store a constructor-like value by the {@link  LiTypeToken} at {@link #instanceCreators}.
 * if the instanceCreators don't have {@link LiTypeToken}, it will foreach the keys to find first the value
 * which key is sub-class of {@link LiTypeToken#getRawType()}. so the {@link  #instanceCreators} is
 * {@link  LinkedHashMap}
 *
 * @author leaderli
 * @since 2022/9/25
 */
public final class ConstructorConstructor {

    private final Map<Type, InstanceCreator<?>> instanceCreators = instanceCreators();

    public ConstructorConstructor() {
    }

    public ConstructorConstructor(LinkedHashMap<Type, InstanceCreator<?>> instanceCreators) {
        // put the same key don't refresh the order, so remove it to avoid the order is not correctly
        this.instanceCreators.keySet().removeIf(instanceCreators::containsKey);
        this.instanceCreators.putAll(instanceCreators);
    }

    static Map<Type, InstanceCreator<?>> instanceCreators() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(ArrayList.class, (InstanceCreator<List<Object>>) type -> new ArrayList<>());
        instanceCreators.put(HashMap.class, (InstanceCreator<HashMap<Object, Object>>) type -> new HashMap<>());
        instanceCreators.put(LinkedHashMap.class, (InstanceCreator<HashMap<Object, Object>>) type -> new LinkedHashMap<>());
        return instanceCreators;
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectConstructor<T> get(LiTypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        InstanceCreator<T> typeCreator = (InstanceCreator<T>) instanceCreators.get(type);
        if (typeCreator != null) {
            return () -> typeCreator.createInstance(type);
        }

        Lino<ObjectConstructor<T>> rawConstructor = Lira.of(instanceCreators.keySet())
                .cast(Class.class)
                .filter(rawType::isAssignableFrom)
                .first()
                .map(instanceCreators::get)
                .map(r -> () -> (T) r.createInstance(type));

        return rawConstructor.get();
    }
}
