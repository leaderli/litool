package io.leaderli.litool.runner;

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

    public final String type;

    public TypeAlias() {
        this.type = "str";
    }

    public TypeAlias(String type) {
        this.type = type;
    }

}
