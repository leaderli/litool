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


private static final Map<Class<?>, Function<String, ?>> CONVERTS = new HashMap<>();

static {

    CONVERTS.put(boolean.class, Boolean::valueOf);
    CONVERTS.put(byte.class, Byte::valueOf);
    CONVERTS.put(double.class, Double::valueOf);
    CONVERTS.put(float.class, Float::valueOf);
    CONVERTS.put(int.class, Integer::valueOf);
    CONVERTS.put(long.class, Long::valueOf);
    CONVERTS.put(short.class, Short::valueOf);

    CONVERTS.put(Boolean.class, Boolean::valueOf);
    CONVERTS.put(Byte.class, Byte::valueOf);
    CONVERTS.put(Double.class, Double::valueOf);
    CONVERTS.put(Float.class, Float::valueOf);
    CONVERTS.put(Integer.class, Integer::valueOf);
    CONVERTS.put(Long.class, Long::valueOf);
    CONVERTS.put(Short.class, Short::valueOf);
    CONVERTS.put(String.class, str -> str);
}

public static boolean support(Class<?> key) {
    return CONVERTS.containsKey(key);
}

public static <T> T parser(String value, T def) {

    Class<T> cls = ClassUtil.getClass(def);

    return parser(cls, value, def);
}

public static <T> T parser(Class<T> cls, String value, T def) {

    return Lino.of(CONVERTS.get(cls))
            .throwable_map(f -> f.apply(value), null)
            .cast(cls)
            .get(def);

}

public static <T> Lino<T> parser(Class<T> cls, String value) {

    return Lino.of(CONVERTS.get(cls))
            .throwable_map(f -> f.apply(value), null)
            .cast(cls);
}
}
