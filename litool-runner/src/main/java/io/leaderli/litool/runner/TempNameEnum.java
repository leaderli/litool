package io.leaderli.litool.runner;

/**
 * @author leaderli
 * @since 2022/8/12
 */
public enum TempNameEnum {

    coordinate(String.class, "");

    public final Class<?> type;
    public final Object def;

    <T> TempNameEnum(Class<T> type, T def) {
        this.type = type;
        this.def = def;
    }

}
