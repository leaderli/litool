package io.leaderli.litool.core.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 不可变的Map，封装了一个Map对象，所有方法通过委托给{@link #delegate}实现
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @since 2022/7/24
 */
public class ImmutableMap<K, V> {

    private final Map<K, V> delegate;

    private ImmutableMap(Map<K, V> delegate) {
        if (delegate == null) {
            delegate = new HashMap<>();
        }
        this.delegate = new HashMap<>(delegate);
    }

    /**
     * 返回一个包含map副本的ImmutableMap实例
     *
     * @param map 一个Map对象
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 一个ImmutableMap对象
     */
    public static <K, V> ImmutableMap<K, V> of(Map<K, V> map) {
        return new ImmutableMap<>(map);
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean containsKey(K key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(V value) {
        return delegate.containsValue(value);
    }

    public V get(K key) {
        return delegate.get(key);
    }

    public Set<K> keySet() {
        return delegate.keySet();
    }

    public Collection<V> values() {
        return delegate.values();
    }

    public V getOrDefault(K key, V defaultValue) {
        return delegate.getOrDefault(key, defaultValue);
    }

    /**
     * 返回一个包含{@link #delegate}副本的Map对象
     *
     * @return {@link #delegate}的副本
     */
    public Map<K, V> toMap() {

        return new HashMap<>(this.delegate);
    }
}
