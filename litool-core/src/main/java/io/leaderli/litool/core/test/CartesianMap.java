package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @param <K> key的类型
 * @param <V> value的类型
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class CartesianMap<K, V> {

    private final Supplier<Map<K, V>> instanceSupplier;
    private final Map<K, V[]> source;


    /**
     * @param instanceSupplier 每个map实例的提供者
     * @param source           数据源
     */
    public CartesianMap(Supplier<Map<K, V>> instanceSupplier, Map<K, V[]> source) {
        this.instanceSupplier = instanceSupplier;
        this.source = source;
    }


    /**
     * @return 所有成员变量有区分度的取值集合的笛卡尔集组成的所有实例
     */

    @SuppressWarnings("unchecked")
    public Map<K, V>[] cartesian() {


        Lira<LiTuple<K, Object[]>> tuples = Lira.of(source.entrySet())
                .map(e -> LiTuple.of(e.getKey(), IterableItr.of(e.getValue()).toArray()))
                .filter(t -> t.getRight().length > 0);

        Object[] keys = tuples.map(LiTuple::_1).toArray(Object.class);
        Object[][] keyValues = tuples.map(LiTuple::_2).map(v -> ArrayUtils.toWrapperArray(Object.class, v)).toArray(Object[].class);
        Object[][] mapCartesian = CollectionUtils.cartesian(keyValues);

        return Lira.of(mapCartesian).map(values -> {


            Map<K, V> map = instanceSupplier.get();
            for (int i = 0; i < keys.length; i++) {
                map.put((K) keys[i], (V) values[i]);
            }
            return map;
        }).toArray(Map.class);
    }


}
