package io.leaderli.litool.core.text;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/9 2:01 PM
 */
public class StringConvert {


    public final static Map<Class<?>, Function<String, ?>> CACHE = new HashMap<>();

    static {

        CACHE.put(boolean.class, Boolean::valueOf);
        CACHE.put(byte.class, Byte::valueOf);
        CACHE.put(double.class, Double::valueOf);
        CACHE.put(float.class, Float::valueOf);
        CACHE.put(int.class, Integer::valueOf);
        CACHE.put(long.class, Long::valueOf);
        CACHE.put(short.class, Short::valueOf);

        CACHE.put(Boolean.class, Boolean::valueOf);
        CACHE.put(Byte.class, Byte::valueOf);
        CACHE.put(Double.class, Double::valueOf);
        CACHE.put(Float.class, Float::valueOf);
        CACHE.put(Integer.class, Integer::valueOf);
        CACHE.put(Long.class, Long::valueOf);
        CACHE.put(Short.class, Short::valueOf);
        CACHE.put(String.class, str -> str);
    }

    public static boolean support(Class<?> key) {
        return CACHE.containsKey(key);
    }

    public static <T> T parser(String value, T def) {

        Class<T> cls = ClassUtil.getClass(def);
        return Lino.of(CACHE.get(cls))
                .throwable_map(f -> f.apply(value))
                .cast(cls)
                .get(def);
    }

    public static <T> Lino<T> parser(Class<T> cls, String value) {

        return Lino.of(CACHE.get(cls))
                .throwable_map(f -> f.apply(value))
                .cast(cls);
    }

    public static <T> Lino<T> parser(Class<T> cls, String value, T def) {

        return Lino.of(CACHE.get(cls))
                .throwable_map(f -> f.apply(value))
                .cast(cls)
                .or(def);

    }
}
