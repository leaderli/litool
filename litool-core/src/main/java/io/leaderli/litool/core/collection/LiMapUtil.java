package io.leaderli.litool.core.collection;


import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiMapUtil {
    /**
     * @param low  低优先级 map
     * @param high 高优先级 map
     * @return 一个新的 map，  他包包含 low 和 high 所有的 key， 其对应的 value
     * 当 high 和 low 都有对应的值且不为 null 时， 使用 high 的， 当有 null 时，优先使用非 null的，
     * 当值的类型都会 map 时，则 使用 两个 map  merge 后的新 map，
     * 使用
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> merge(Map low, Map high) {


        Map result = new HashMap<>(low);
        high.forEach((k, v) -> {
            if (v != null) {
                if (v instanceof Map) {

                    Object exist = result.get(k);

                    if (exist instanceof Map) {


                        result.put(k, merge((Map) exist, (Map) v));
                        return;

                    }

                }

                result.put(k, v);
            }
        });

        Map<String, Object> stringObjectMap = new HashMap<>();
        result.forEach((k, v) -> stringObjectMap.put(String.valueOf(k), v));
        return stringObjectMap;
    }

    /**
     * @param origin   源 map
     * @param override 覆盖 map
     * @return 一个新的 map，  使用 override 覆盖 origin 的 同名 key 的值，不会覆盖 origin 不存在的 key 的值，不会使用 null 去覆盖，
     * 对于 key 对应的 value 都为 map 类型，则替换位 override 两个 map 后的新 map
     */

    @SuppressWarnings({"unchecked", "rawtypes"})

    public static Map<String, Object> override(Map origin, Map override) {


        Map result = new HashMap<>(origin);
        origin.forEach((k, ori) -> {

            Object over = override.get(k);

            if (over != null) {

                if (over instanceof Map) {


                    if (ori instanceof Map) {

                        result.put(k, override((Map) ori, (Map) over));
                        return;

                    }

                }

                result.put(k, over);
            }
        });

        Map<String, Object> stringObjectMap = new HashMap<>();
        result.forEach((k, v) -> stringObjectMap.put(String.valueOf(k), v));
        return stringObjectMap;


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
     * @param map          map
     * @param key          键
     * @param listItemType 集合的元素类型
     * @param <T>          集合的元素泛型
     * @return 根据 key，查询一个 {@code List<T>} ，筛选 list 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     */
    public static <T> List<T> getTypeList(Map<String, ?> map, String key, Class<? extends T> listItemType) {

        if (map == null) {
            return CollectionUtils.emptyList();
        }
        Object value = map.get(key);

        if (value instanceof List) {

            return Lira.of((List<?>) value).<T>cast(listItemType).get();
        }
        return new ArrayList<>();
    }

    /**
     * @param map       map
     * @param key       键
     * @param valueType 值的类型
     * @param <V>       值的泛型
     * @return 根据 key，查询一个 {@code Map<String,V>}，筛选 map 中满足指定 class 类型的数据。当查询不到或者无满足 class 类型的数据，会返回一个空的集合
     * @see #getTypeMap(Map, String, Class, Class)
     */
    public static <V> Map<String, V> getTypeMap(Map<String, ?> map, String key, Class<? extends V> valueType) {
        return getTypeMap(map, key, String.class, valueType);
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
    public static <K, V> Map<K, V> getTypeMap(Map<String, ?> map, String key, Class<? extends K> keyType, Class<? extends V> valueType) {

        if (map == null) {
            return new HashMap<>();
        }
        Object value = map.get(key);

        if (value instanceof Map) {
            return ClassUtil.filterCanCast((Map<?, ?>) value, keyType, valueType);
        }

        return new HashMap<>();
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
     * @param map map
     * @param key 键
     * @return 根据 key，查询指定 String 类型的值，当查询不到或类型不匹配时，返回空
     * @see #getTypeList(Map, String, Class)
     */
    public static Lino<String> getTypeObject(Map<String, String> map, String key) {
        return getTypeObject(map, key, String.class);
    }

    /**
     * @param map      map
     * @param key      键
     * @param itemType 值的类型
     * @param <T>      值的泛型
     * @return 根据 key，查询指定 class 类型的值，当查询不到或类型不匹配时，返回空
     */
    public static <T> Lino<T> getTypeObject(Map<String, ?> map, String key, Class<? extends T> itemType) {
        if (map == null) {
            return Lino.none();
        }
        return Lino.of(map.get(key)).cast(itemType);
    }


}
