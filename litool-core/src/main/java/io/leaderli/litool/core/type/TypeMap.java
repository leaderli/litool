package io.leaderli.litool.core.type;


import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * a map like class use class type as key to store value, provide type-safe {@link  #put(Class, Object)},
 * {@link  #get(Class)} method
 * <p>
 * to avoid auto-box, only use wrapper class
 */
public class TypeMap {

    private final Map<Class<?>, Object> proxy = new HashMap<>();

    public <T> Lino<T> computeIfAbsent(Class<T> type, Supplier<T> supplier) {
        return this.get(type).or(() -> this.put(type, supplier.get()));
    }

    /**
     * Return the value stored by class
     *
     * @param type the key
     * @param <T>  the type parameter of key and  the type of value
     * @return the value stored by class
     */
    @SuppressWarnings("unchecked")
    public <T> Lino<T> get(Class<T> type) {
        // 基本类型在 LiTypeMap 会被装箱，因此需要使用其包装类去查找
        return Lino.of((T) this.proxy.get(ClassUtil.primitiveToWrapper(type)));
    }

    /**
     * Store the key-value
     *
     * @param type  the key
     * @param value the value
     * @param <T>   the type parameter of key and  the type of value
     * @return the value
     */
    public <T> T put(Class<T> type, T value) {
        this.proxy.put(ClassUtil.primitiveToWrapper(type), value);
        return value;
    }

    /**
     * Remove the key and relative value from type map
     *
     * @param type the key
     * @param <T>  the type parameter of key and  the type of value
     */
    public <T> void remove(Class<T> type) {
        this.proxy.remove(ClassUtil.primitiveToWrapper(type));
    }

    @Override
    public String toString() {
        return this.proxy.toString();
    }
}
