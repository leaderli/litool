package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;

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
public class LiTupleMap<K, V> {

    private final List<LiTuple2<K, V>> paris = new ArrayList<>();


    public static <K, V> LiTupleMap<K, V> of() {

        return new LiTupleMap<>();
    }

    public static <K, V> LiTupleMap<K, V> of(K k, V v) {
        LiTupleMap<K, V> of = of();
        return of.putKeyValue(k, v);
    }

    public Lino<V> getValueByKey(K key) {

        for (LiTuple2<K, V> tuple : paris) {

            if (Objects.equals(key, tuple._1)) {

                return Lino.of(tuple._2);
            }
        }
        return Lino.none();
    }

    public LiTupleMap<K, V> putKeyValue(K key, V value) {

        removeByKey(key);
        paris.add(LiTuple.of(key, value));
        return this;

    }

    public void removeByKey(K key) {
        paris.removeIf(tuple -> Objects.equals(key, tuple._1));
    }

    public Lino<K> getKeyByValue(V value) {
        for (LiTuple2<K, V> tuple : paris) {

            if (Objects.equals(value, tuple._2)) {

                return Lino.of(tuple._1);
            }
        }
        return Lino.none();
    }

    public LiTupleMap<K, V> putValueKey(V value, K key) {
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
}
