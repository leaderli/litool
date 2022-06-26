package io.leaderli.litool.core.collection;


import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiClassUtil;

import java.util.*;

public class LiMapUtil {

    /**
     * @param origin   源 map
     * @param override 覆盖 map
     * @param <K>      map 的键的泛型
     * @param <V>      map 的值的泛型
     * @return 一个新的 map，  使用 override 覆盖 origin 的 同名 key 的数组，不会覆盖 origin 不存在的 key 的值，不会使用 null 去覆盖
     */
    public static <K, V> Map<K, V> override(Map<K, V> origin, Map<K, V> override) {

        return _override(origin, override);
    }

    @SuppressWarnings("all")
    private static <T> T _override(T origin, T override) {

        if (origin == null) {
            return override;
        }

        if (override == null) {
            return origin;
        }

        if (origin instanceof Map && override instanceof Map) {

            Map result = new HashMap((Map) origin);
            result.forEach((k, v) -> result.put(k, _override(v, ((Map) override).get(k))));
            return (T) result;
        }

        return override;

    }

    /**
     * @param map          map
     * @param key          键
     * @param listItemType 集合的元素类型
     * @param <T>          集合的元素泛型
     * @return 根据 key，查询一个 {@code List<T>} ，筛选 list 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     */
    public static <T> List<T> getTypeList(Map<String, ?> map, String key, Class<T> listItemType) {

        if (map == null) {
            return LiListUtil.emptyList();
        }
        Object value = map.get(key);

        if (value instanceof List) {

            //noinspection
            return Lira.of((List<?>) value).cast(listItemType).getRaw();
        }
        return new ArrayList<>();
    }

    /**
     * @param map map
     * @param key 键
     * @return 根据 key，查询一个 {@code List<String>} ，筛选 list 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     */
    public static List<String> getTypeList(Map<String, ?> map, String key) {
        return getTypeList(map, key, String.class);
    }


    /**
     * @param map       map
     * @param key       键
     * @param keyType   键的类型
     * @param valueType 值的类型
     * @param <K>       键的泛型
     * @param <V>       值的泛型
     * @return 根据 key，查询一个 {@code Map<K,V>}，筛选 map 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     */
    public static <K, V> Map<K, V> getTypeMap(Map<String, ?> map, String key, Class<K> keyType, Class<V> valueType) {

        if (map == null) {
            return new HashMap<>();
        }
        Object value = map.get(key);

        if (value instanceof Map) {
            return LiClassUtil.filterCanCast((Map<?, ?>) value, keyType, valueType);
        }

        return new HashMap<>();
    }

    /**
     * @param map       map
     * @param key       键
     * @param valueType 值的类型
     * @param <V>       值的泛型
     * @return 根据 key，查询一个 {@code Map<String,V>}，筛选 map 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     * @see #getTypeMap(Map, String, Class, Class)
     */
    public static <V> Map<String, V> getTypeMap(Map<String, ?> map, String key, Class<V> valueType) {
        return getTypeMap(map, key, String.class, valueType);
    }

    /**
     * @param map map
     * @param key 键
     * @return 根据 key，查询一个 {@code Map<String,String>}，筛选 map 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     * @see #getTypeMap(Map, String, Class, Class)
     */
    public static Map<String, String> getTypeMap(Map<String, ?> map, String key) {
        return getTypeMap(map, key, String.class, String.class);
    }

    /**
     * @param map      map
     * @param key      键
     * @param itemType 值的类型
     * @param <T>      值的泛型
     * @return 根据 key，查询指定 class 类型的值，当查询不到或类型不匹配时，返回空
     */
    public static <T> Lino<T> getTypeObject(Map<String, ?> map, String key, Class<T> itemType) {
        return Lino.of(map).map(to -> LiClassUtil.cast(map.get(key), itemType));
    }

    /**
     * @param map map
     * @param key 键
     * @return 根据 key，查询指定 String 类型的值，当查询不到或类型不匹配时，返回空
     * @see #getTypeList(Map, String, Class)
     */
    public static Lino<String> getTypeObject(Map<String, String> map, String key) {
        return getTypeObject(map, key, String.class);
    }


    /**
     * @param origin 对象
     * @param keys   嵌套 key
     * @return 多次嵌套的元素
     */
    public static Lino<?> deepGet(Object origin, String... keys) {


        if (keys == null || keys.length == 0) {
            return Lino.none();
        }

        String key = keys[0];

        Lino<Object> value = Lino.of(origin)
                .cast(Map.class)
                .map(m -> m.get(key));

        keys = Arrays.copyOfRange(keys, 1, keys.length);

        if (keys.length == 0) {
            return value;
        }
        return deepGet(value.get(), keys);

    }


}
