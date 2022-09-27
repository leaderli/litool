package io.leaderli.litool.core.text;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * a tool that convert string to specifier class
 *
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

    /**
     * Return class is support to converted
     *
     * @param cls the class
     * @return class is support to converted
     */
    public static boolean support(Class<?> cls) {
        return CONVERTS.containsKey(cls);
    }


    /**
     * Return the def  if parsed fail, or the parsed value
     *
     * @param value the string value
     * @param def   the def value if parsed fail
     * @param <T>   the type parameter of parsed type, it's same with def type
     * @return the parsed value
     */
    public static <T> T parser(String value, T def) {

        Class<T> cls = ClassUtil.getDeclaringClass(def);

        return parser(cls, value, def);
    }

    /**
     * Return the def  if parsed fail, or the parsed value
     *
     * @param cls   the type of parsed value
     * @param value the string value
     * @param def   the def value if parsed fail
     * @param <T>   the type parameter of parsed type
     * @return the parsed value
     */
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
