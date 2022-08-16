package io.leaderli.litool.runner;

import io.leaderli.litool.runner.constant.OperatorEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/24
 * TODO 校验literal
 */
public class TypeAlias {

    public final static Map<String, Class<?>> ALIAS = new HashMap<>();
    public final static Map<String, Function<String, ?>> TYPE_CONVERT = new HashMap<>();

    static {
        put("int", Integer.class, Integer::valueOf);
        put("double", Double.class, Double::valueOf);
        put("str", String.class, s -> s);
        put("boolean", Boolean.class, Boolean::valueOf);
        put("op", OperatorEnum.class, OperatorEnum::get);
    }

    public static <T> void put(String key, Class<T> type, Function<String, T> function) {
        ALIAS.put(key, type);
        TYPE_CONVERT.put(key, function);

    }

    public static Class<?> getType(String type) {
        return ALIAS.get(type);
    }

    public static Object parser(String type, String value, String def) {

        Function<String, ?> function = TYPE_CONVERT.get(type);
        try {
            Object apply = function.apply(value);
            if (apply == null) {
                return def;
            }
            return apply;

        } catch (Throwable throwable) {
            return function.apply(def);
        }

    }

    public static Object parser(String type, String value) {

        return TYPE_CONVERT.get(type).apply(value);
    }

    public static void check(String type, String value, String error) {

        try {
            TYPE_CONVERT.get(type).apply(value);
        } catch (Throwable throwable) {
            throw new RuntimeException(error);
        }
    }

    public static boolean support(String type) {
        return ALIAS.containsKey(type);
    }

    public static boolean support(Class<?> type) {
        return ALIAS.containsValue(type);
    }

}
