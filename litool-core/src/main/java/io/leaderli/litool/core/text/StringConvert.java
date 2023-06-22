package io.leaderli.litool.core.text;

import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 一个将字符串转换为指定类的工具
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
     * 返回是否支持转换的类
     *
     * @param cls 要转换的类
     * @return 是否支持转换
     */
    public static boolean support(Class<?> cls) {
        return CONVERTS.containsKey(cls);
    }


    /**
     * 如果解析失败,返回def,否则返回解析的值
     *
     * @param value 要解析的字符串值
     * @param def   如果解析失败的默认值
     * @param <T>   解析类型的参数类型,与def类型相同
     * @return 解析的值
     * @throws NullPointerException 如果def为null
     */
    @SuppressWarnings("unchecked")
    public static <T> T parser(String value, T def) {
        Class<T> cls = (Class<T>) def.getClass();
        return parser(cls, value, def);
    }

    /**
     * 如果解析失败,返回def,否则返回解析的值
     *
     * @param cls   要解析的值的类型
     * @param value 要解析的字符串值
     * @param def   如果解析失败的默认值
     * @param <T>   解析类型的参数类型
     * @return 解析的值
     */
    public static <T> T parser(Class<T> cls, String value, T def) {

        return Lino.of(CONVERTS.get(cls))
                .mapIgnoreError(f -> f.apply(value), null)
                .cast(cls)
                .get(def);

    }

    /**
     * 如果解析失败,返回 {@link  Lino#none()},否则返回解析的值
     *
     * @param cls   要解析的值的类型
     * @param value 要解析的字符串值
     * @param <T>   解析类型的参数类型
     * @return 解析的值
     */
    public static <T> Lino<T> parser(Class<T> cls, String value) {

        return Lino.of(CONVERTS.get(cls))
                .mapIgnoreError(f -> f.apply(value), null)
                .cast(cls);
    }
}
