package io.leaderli.litool.core.collection;


import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Map工具类，提供了一些方便的方法，用于合并、覆盖、过滤Map等操作。
 */
public class LiMapUtil {

    /**
     * 合并两个Map，将两个Map中的键合并成一个新Map，并根据一定规则选择非null的值作为新Map中的值。
     * <p>
     * 如果两个Map中的值都不为null，并且值不是Map类型，将优先选择high中的值。
     * <p>
     * 如果两个Map中的值都是Map类型，则递归调用merge方法合并Map。
     *
     * @param low  低优先级Map
     * @param high 高优先级Map
     * @param <K>  map 的 key 类型
     * @param <V>  map 的 value 类型
     * @return 合并后的新Map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> Map<K, V> merge(Map<K, V> low, Map<K, V> high) {


        if (low == null) {
            low = new HashMap();
        }
        if (high == null) {
            high = new HashMap();
        }
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

        return result;

    }

    /**
     * 用replace Map覆盖origin Map，返回一个新的Map。
     * <p>
     * 新Map中的键与origin Map相同，值则根据一定规则选择非null的值作为新Map中的值。
     * <p>
     * 如果两个Map中的值都不为null，并且值不是Map类型，将选择replace中的值。
     * <p>
     * 如果两个Map中的值都是Map类型，则递归调用override方法覆盖Map。
     *
     * @param origin  原始Map
     * @param replace 覆盖Map
     * @param <K>     map 的 key 类型
     * @param <V>     map 的 value 类型
     * @return 覆盖后的新Map
     */


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> Map<K, V> override(Map<K, V> origin, Map<K, V> replace) {


        if (origin == null) {
            origin = new HashMap<>();
        }
        Map<K, V> result = new HashMap<>(origin);
        if (replace == null) {
            return result;
        }
        origin.forEach((key, value) -> {

            V over = replace.get(key);

            if (over != null) {

                if (over instanceof Map && value instanceof Map) {

                    Map<Object, Object> override = override((Map) value, (Map) over);
                    result.put(key, (V) override);
                    return;

                }

                result.put(key, over);
            }
        });

        return result;

    }

    /**
     * 从Map中获取键为key的值，如果值为List，则过滤其中的String元素并返回。
     * 如果值不是List，则返回一个空List。
     *
     * @param map 搜索的Map
     * @param key Map中的键
     * @param <K> map 的 key 类型
     * @param <V> map 的 value 类型
     * @return 如果值为List，则过滤其中的String元素并返回，否则返回一个空List
     * @see #getTypeList(Map, Object, Class)
     */
    public static <K, V> List<String> getTypeList(Map<K, V> map, K key) {
        return getTypeList(map, key, String.class);
    }

    /**
     * 从Map中获取键为key的值，如果值为List，则过滤其中的指定类型的元素并返回。
     * 如果值不是List，则返回一个空List。
     *
     * @param map          搜索的Map
     * @param key          Map中的键
     * @param listItemType 返回的List元素类型
     * @param <T>          List元素类型的泛型
     * @param <K>          map 的 key 类型
     * @param <V>          map 的 value 类型
     * @return 如果值为List，则过滤其中的指定类型的元素并返回，否则返回一个空List
     */
    public static <K, V, T> List<T> getTypeList(Map<K, V> map, K key, Class<? extends T> listItemType) {

        return getTypeObject(map, key, List.class)
                .toLira()
                .<T>cast(listItemType).get();
    }

    /**
     * 从Map中获取键为key的值，并将其转换为指定类型，如果类型不匹配则返回Lino.none()。
     *
     * @param map      搜索的Map
     * @param key      Map中的键
     * @param itemType 返回的值的类型
     * @param <T>      返回值类型的泛型
     * @param <K>      map 的 key 类型
     * @param <V>      map 的 value 类型
     * @return 如果值类型匹配则返回值，否则返回Lino.none()
     */

    public static <K, V, T> Lino<T> getTypeObject(Map<K, V> map, K key, Class<? extends T> itemType) {
        if (map == null) {
            return Lino.none();
        }
        return Lino.of(map.get(key)).cast(itemType);
    }


    /**
     * 根据给定的Map和Key获取对应的值，如果值为String类型，则返回该值；
     * 如果值不为String类型，则返回{@link Lino#none()}。
     *
     * @param map 给定的Map对象
     * @param key Map中对应的Key值
     * @param <K> map 的 key 类型
     * @param <V> map 的 value 类型
     * @return 根据给定的Map和Key获取对应的值，如果值为String类型，则返回该值；
     * 如果值不为String类型，则返回{@link Lino#none()}。
     * @see #getTypeObject(Map, Object, Class)
     */
    public static <K, V> Lino<String> getTypeObject(Map<K, V> map, K key) {
        return getTypeObject(map, key, String.class);
    }


    public static <K, V> HashMap<K, V> newHashMap(K k, V v) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(K k, V v) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k, v);
        return map;
    }
}
