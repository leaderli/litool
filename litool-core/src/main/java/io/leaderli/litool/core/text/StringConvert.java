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
        CACHE.put(Boolean.class, Boolean::valueOf);
        CACHE.put(Byte.class, Byte::valueOf);
        CACHE.put(Double.class, Double::valueOf);
        CACHE.put(Float.class, Float::valueOf);
        CACHE.put(Integer.class, Integer::valueOf);
        CACHE.put(Long.class, Long::valueOf);
        CACHE.put(Short.class, Short::valueOf);
    }

    public static <T> T parser(String value, T def) {

        Class<T> cls = ClassUtil.getClass(def);
        return Lino.of(CACHE.get(cls))
                .throwable_map(f -> f.apply(value))
                .cast(cls)
                .get(def);
    }
}
