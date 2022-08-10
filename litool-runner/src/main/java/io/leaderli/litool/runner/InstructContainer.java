package io.leaderli.litool.runner;

import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.runner.instruct.Instruct;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InstructContainer {

    private static final Map<String, Instruct> ALIAS_METHOD;
    private static final Map<String, Method> NAME_INSTRUCT = new HashMap<>();

    static {
        ALIAS_METHOD = scanner();
    }


    public static Map<String, Instruct> scanner() {
        return ClassScanner.getSubTypesOf(Instruct.class, Instruct.class)
                .map(cls -> ReflectUtil.newInstance(cls).get())
                .toMap(
                        Instruct::name,
                        instruct -> instruct
                );


    }

    public static Instruct getInnerMethodByAlias(String alias) {
        return ALIAS_METHOD.get(alias);
    }


}
