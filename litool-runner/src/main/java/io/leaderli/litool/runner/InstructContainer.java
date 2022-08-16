package io.leaderli.litool.runner;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.runner.instruct.Instruct;

import java.util.Map;

public class InstructContainer {

    private static final Map<String, Instruct> ALIAS_METHOD;

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

    /**
     * 提供注册指令的功能
     *
     * @param instruct 指令
     */
    public static void registerInstruct(Instruct instruct) {
        LiAssertUtil.assertFalse(ALIAS_METHOD.containsKey(instruct.name()), String.format("instruct %s already exists", instruct.name()));
        ALIAS_METHOD.put(instruct.name(), instruct);
    }


}
