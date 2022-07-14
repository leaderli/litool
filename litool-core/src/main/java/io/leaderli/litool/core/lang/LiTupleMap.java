package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/14 10:22 AM
 * <p>
 * 键值对一一对应的 map
 */
public class LiTupleMap<K, V> {

    List<LiTuple2<K, V>> paris = new ArrayList<>();


    public Lino<V> getValueByKey(K key) {

        for (LiTuple2<K, V> tuple : paris) {

            if (Objects.equals(key, tuple._1)) {

                return Lino.of(tuple._2);
            }
        }
        return Lino.none();
    }

    public void putKeyValue(K key, V value) {

        removeByKey(key);
        paris.add(LiTuple.of(key, value));

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

    public void putValueKey(V value, K key) {
        removeByValue(value);
        paris.add(LiTuple.of(key, value));
    }

    public void removeByValue(V value) {
        paris.removeIf(tuple -> Objects.equals(value, tuple._2));
    }


    public void clear() {
        paris.clear();
    }

}
