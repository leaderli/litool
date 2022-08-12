package io.leaderli.litool.runner;

import io.leaderli.litool.runner.constant.UnitStateConstant;

/**
 * @author leaderli
 * @since 2022/8/12
 */
public enum TempNameEnum {

    unit_state(Integer.class, UnitStateConstant.CONTINUE),

    coordinate(String.class, "");


    public final Class<?> type;
    public final Object def;

    <T> TempNameEnum(Class<T> type, T def) {
        this.type = type;
        this.def = def;
    }

}
