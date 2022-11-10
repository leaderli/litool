package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    private final LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    public ConstructorConstructor() {
        this(new LinkedHashMap<>());
    }

    public ConstructorConstructor(LinkedHashMap<Type, InstanceCreator<?>> tail) {
        this(new LinkedHashMap<>(), tail);
    }

    public ConstructorConstructor(LinkedHashMap<Type, InstanceCreator<?>> head, LinkedHashMap<Type, InstanceCreator<?>> tail) {

        LinkedHashMap<Type, InstanceCreator<?>> temp = head;
        instanceCreators.putAll(temp);

        temp = generateDefaultInstanceCreators();
        // avoid default instanceCreators to reset the key-value in head, so remove it from default instanceCreators
        instanceCreators.keySet().forEach(temp::remove);
        instanceCreators.putAll(temp);

        temp = tail;
        // put the same key don't refresh the order, so remove it to avoid the order is not correctly
        instanceCreators.keySet().removeIf(temp::containsKey);
        instanceCreators.putAll(temp);
    }

    private static LinkedHashMap<Type, InstanceCreator<?>> generateDefaultInstanceCreators() {
        LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
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
