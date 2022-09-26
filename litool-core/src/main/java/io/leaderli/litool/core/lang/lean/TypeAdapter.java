package io.leaderli.litool.core.lang.lean;

/**
 * @author leaderli
 * @since 2022/9/24 2:47 PM
 */
public interface TypeAdapter<T> {

    /**
     * @param source the source
     * @return create target by source
     */
    T read(Object source);
}
