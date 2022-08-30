package io.leaderli.litool.core.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A ImmutableMap which contains a map, all method are delegated throw {@link  #delegate}
 *
 * @param <K> the type of key
 * @param <V> the type of value
 * @author leaderli
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
     * Provide ImmutableMap instance which contains a copy of {@code  map}
     *
     * @param map a map
     * @param <K> the type of key
     * @param <V> the type of value
     * @return a ImmutableMap
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
     * Return a clone of {@link  #delegate}
     *
     * @return a clone of {@link  #delegate}
     */
    public Map<K, V> toMap() {

        return new HashMap<>(this.delegate);
    }
}
