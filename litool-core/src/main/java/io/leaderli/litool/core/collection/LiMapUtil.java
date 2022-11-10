package io.leaderli.litool.core.collection;


import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * a handy tool of map
 */
public class LiMapUtil {

    /**
     * Merge two map, which keys is union of two map , the values will chose a non-null value get by key,
     * if both map have the value and value is not map, will use high priority one. if both value are map,
     * will recursive call merge method
     *
     * @param low  low priority map
     * @param high high priority map
     * @return a new merged map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> merge(Map low, Map high) {


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

        Map<String, Object> stringObjectMap = new HashMap<>();
        result.forEach((k, v) -> stringObjectMap.put(String.valueOf(k), v));
        return stringObjectMap;
    }


    /**
     * Return a new map which is to use replace map override origin map.
     * the new map keys is same as origin map , the values will chose a non-null value get by key,
     * if both map have the value and value is not map, will use replace . if both value are map,
     * will recursive call override method
     *
     * @param origin  the origin map
     * @param replace the replace map
     * @return a new replace map
     */

    @SuppressWarnings({"unchecked", "rawtypes"})

    public static Map<String, Object> override(Map origin, Map replace) {


        if (origin == null) {
            origin = new HashMap();
        }
        Map result = new HashMap<>(origin);
        if (replace == null) {
            return result;
        }
        origin.forEach((key, value) -> {

            Object over = replace.get(key);

            if (over != null) {

                if (over instanceof Map && value instanceof Map) {

                    result.put(key, override((Map) value, (Map) over));
                    return;

                }

                result.put(key, over);
            }
        });

        Map<String, Object> stringObjectMap = new HashMap<>();
        result.forEach((k, v) -> stringObjectMap.put(String.valueOf(k), v));
        return stringObjectMap;


    }

    /**
     * Return  {@code  List<String} if the found value is List  filter elements of String
     * otherwise return a empty List
     *
     * @param map the searched map
     * @param key the key of map
     * @return {@code  List<String} if the found value is List  filter elements of String otherwise Return a
     * empty List
     * @see #getTypeList(Map, String, Class)
     */
    public static List<String> getTypeList(Map<String, ?> map, String key) {
        return getTypeList(map, key, String.class);
    }

    /**
     * Return {@code  List<T} if the found value is List  filter elements of type T
     * otherwise return a empty List
     *
     * @param map          the searched map
     * @param key          the key of map
     * @param listItemType the class of Returned List element
     * @param <T>          the generic type of Returned List element
     * @return {@code  List<T} if the found value is List  filter elements of type T otherwise Return a
     * empty List
     */
    public static <T> List<T> getTypeList(Map<String, ?> map, String key, Class<? extends T> listItemType) {

        return getTypeObject(map, key, List.class)
                .toLira()
                .<T>cast(listItemType).get();
    }

    /**
     * Return {@code  T} if the found value is instanceof T otherwise
     * return  {@link  Lino#none()}
     *
     * @param map      the searched map
     * @param key      the key of searched map
     * @param itemType the class of found value
     * @param <T>      the generic type of found value
     * @return {@code  T} if the found value is instanceof T otherwise return {@link  Lino#none()}
     */

    public static <T> Lino<T> getTypeObject(Map<String, ?> map, String key, Class<? extends T> itemType) {
        if (map == null) {
            return Lino.none();
        }
        return Lino.of(map.get(key)).cast(itemType);
    }

    /**
     * Return {@code  Map<String,V} if the found value is Map and filter elements of type String,V
     * otherwise Return a empty  map
     *
     * @param map       the searched map
     * @param key       the key of searched map
     * @param valueType class of the found map value
     * @param <V>       type of the found map value
     * @return {@code  Map<String,V} if the found value is Map and filter elements of type String,V otherwise Return a
     * empty  map
     * @see #getTypeMap(Map, String, Class, Class)
     */
    public static <V> Map<String, V> getTypeMap(Map<String, ?> map, String key, Class<? extends V> valueType) {
        return getTypeMap(map, key, String.class, valueType);
    }

    /**
     * Return {@code  Map<K,V} if the found value is Map and filter elements of type K,V otherwise
     * Return a empty  map
     *
     * @param map       the searched map
     * @param key       the key of searched map
     * @param keyType   class of the found map key
     * @param valueType class of the found map value
     * @param <K>       type of the found map key
     * @param <V>       type of the found map value
     * @return {@code  Map<K,V} if the found value is Map and filter elements of type K,V otherwise Return a
     * empty  map
     * @see #getTypeObject(Map, String)
     */
    public static <K, V> Map<K, V> getTypeMap(Map<String, ?> map, String key, Class<? extends K> keyType, Class<?
            extends V> valueType) {

        return getTypeObject(map, key, Map.class).<K, V>cast(keyType, valueType).get(new HashMap<>());
    }

    /**
     * Return {@code  Map<String,String} if the found value is Map and filter elements of type String,String
     * otherwise return a empty  map
     *
     * @param map the searched map
     * @param key the key of searched map
     * @return {@code  Map<String,String} if the found value is Map and filter elements of type String,String otherwise
     * Return a
     * empty  map
     * @see #getTypeMap(Map, String, Class, Class)
     */
    public static Map<String, String> getTypeMap(Map<String, ?> map, String key) {
        return getTypeMap(map, key, String.class, String.class);
    }

    /**
     * Return {@code  String} if the found value is instanceof String otherwise
     * return  {@link  Lino#none()}
     *
     * @param map the searched map
     * @param key the key of searched map
     * @return {@code  String} if the found value is instanceof String otherwise return {@link  Lino#none()}
     * @see #getTypeObject(Map, String, Class)
     */
    public static Lino<String> getTypeObject(Map<String, ?> map, String key) {
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
