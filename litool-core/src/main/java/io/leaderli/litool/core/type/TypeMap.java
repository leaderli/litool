package io.leaderli.litool.core.type;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 类似于Map的数据结构，使用类类型作为键来存储值，提供类型安全的 put 和 get 方法。
 * 为了避免自动装箱，仅使用包装类。
 */
public class TypeMap {

    private final Map<Class<?>, Object> proxy = new HashMap<>();

    /**
     * 如果TypeMap中不存在该类型的值，则使用指定的Supplier生成新值并将其存储在TypeMap中。
     *
     * @param <T>      值的类型
     * @param type     值的类型
     * @param supplier 新值的生成器
     * @return Lino对象，其中包含从TypeMap中获取到的新值
     */
    @SuppressWarnings("unchecked")
    public <T> T computeIfAbsent(Class<T> type, Supplier<T> supplier) {
        return (T) proxy.computeIfAbsent(type, k -> supplier.get());
    }

    /**
     * 获取TypeMap中存储的值。
     *
     * @param <T>  值的类型
     * @param type 值的类型
     * @return Lino对象，其中包含从TypeMap中获取到的值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        // 基本类型在 LiTypeMap 会被装箱，因此需要使用其包装类去查找
        return (T) this.proxy.get(ClassUtil.primitiveToWrapper(type));
    }

    /**
     * 存储键值对到TypeMap中。
     * @param <T> 值的类型
     * @param type 键的类型
     * @param value 值
     * @return 存储的值
     */
    public <T> T put(Class<T> type, T value) {
        this.proxy.put(ClassUtil.primitiveToWrapper(type), value);
        return value;
    }


    /**
     * 从TypeMap中删除键和相关值。
     * @param <T> 值的类型
     * @param type 键的类型
     */
    public <T> void remove(Class<T> type) {
        this.proxy.remove(ClassUtil.primitiveToWrapper(type));
    }

    @Override
    public String toString() {
        return this.proxy.toString();
    }
}
