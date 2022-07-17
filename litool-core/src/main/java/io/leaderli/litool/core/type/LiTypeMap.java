package io.leaderli.litool.core.type;


import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 以 class 作为 key 存储 该 class 的某个唯一实例
 */
public class LiTypeMap {

    private final Map<Class<?>, Object> proxy = new HashMap<>();

    public <T> Lino<T> computeIfAbsent(Class<T> type, Supplier<T> supplier) {
        return this.get(type).or(() -> this.put(type, supplier.get()));
    }

    @SuppressWarnings("unchecked")
    public <T> Lino<T> get(Class<T> type) {
        // 基本类型在 LiTypeMap 会被装箱，因此需要使用其包装类去查找
        return Lino.of((T) this.proxy.get(LiClassUtil.primitiveToWrapper(type)));
    }

    public <T> T put(Class<T> type, T value) {
        this.proxy.put(LiClassUtil.primitiveToWrapper(type), value);
        return value;
    }

    public <T> void remove(Class<T> type) {
        this.proxy.remove(type);
    }

    @Override
    public String toString() {
        return this.proxy.toString();
    }
}
