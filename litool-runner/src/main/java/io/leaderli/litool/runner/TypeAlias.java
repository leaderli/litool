package io.leaderli.litool.runner;

import io.leaderli.litool.core.text.StringConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class TypeAlias {

    public final static Map<String, Class<?>> ALIAS = new HashMap<>();

    static {
        ALIAS.put("int", Integer.class);
        ALIAS.put("str", String.class);
    }

    public static Class<?> getType(String type) {
        return ALIAS.get(type);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object parser(String type, String value, String def) {

        Class cls = ALIAS.get(type);
        return StringConvert.parser(cls, value)
                .or(() -> StringConvert.parser(cls, def).get())
                .get();
    }

    public static boolean support(String type) {
        return ALIAS.containsKey(type);
    }

}
