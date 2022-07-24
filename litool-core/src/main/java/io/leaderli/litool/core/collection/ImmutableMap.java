package io.leaderli.litool.core.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class ImmutableMap<K, V> {

    private final Map<K, V> map;

    private ImmutableMap(Map<K, V> map) {
        this.map = new HashMap<>(map);
    }

    public static <K, V> ImmutableMap<K, V> of(Map<K, V> map) {
        return new ImmutableMap<>(map);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public V getOrDefault(K key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public Map<K, V> copy() {

        return new HashMap<>(this.map);
    }
}
