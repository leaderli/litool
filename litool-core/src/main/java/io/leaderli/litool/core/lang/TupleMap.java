package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author leaderli
 * @since 2022/7/14 10:22 AM
 * <p>
 * 键值对一一对应的 map
 */
public class TupleMap<K, V> implements LiValue {

private final List<LiTuple2<K, V>> paris = new ArrayList<>();

public static <K, V> TupleMap<K, V> of(K k, V v) {
    TupleMap<K, V> of = of();
    return of.putKeyValue(k, v);
}

public static <K, V> TupleMap<K, V> of() {

    return new TupleMap<>();
}

public TupleMap<K, V> putKeyValue(K key, V value) {

    removeByKey(key);
    paris.add(LiTuple.of(key, value));
    return this;

}

public void removeByKey(K key) {
    paris.removeIf(tuple -> Objects.equals(key, tuple._1));
}

public Lino<V> getValueByKey(K key) {

    for (LiTuple2<K, V> tuple : paris) {

        if (Objects.equals(key, tuple._1)) {

            return Lino.of(tuple._2);
        }
    }
    return Lino.none();
}

public Lino<K> getKeyByValue(V value) {
    for (LiTuple2<K, V> tuple : paris) {

        if (Objects.equals(value, tuple._2)) {

            return Lino.of(tuple._1);
        }
    }
    return Lino.none();
}

public TupleMap<K, V> putValueKey(V value, K key) {
    removeByValue(value);
    paris.add(LiTuple.of(key, value));

    return this;

}

public void removeByValue(V value) {
    paris.removeIf(tuple -> Objects.equals(value, tuple._2));
}


public void clear() {
    paris.clear();
}

public void removeIf(Predicate<LiTuple2<K, V>> predicate) {
    paris.removeIf(predicate);
}

public Lira<K> keySet() {
    return Lira.of(paris).map(LiTuple2::_1);
}

public Lira<V> ValueSet() {
    return Lira.of(paris).map(LiTuple2::_2);
}

public Lira<LiTuple2<K, V>> entrySet() {
    return Lira.of(paris);
}

@Override
public boolean present() {
    return size() > 0;
}

public int size() {
    return paris.size();
}

@Override
public String name() {
    return "tuple_map";
}
}
