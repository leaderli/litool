package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiValue {

    /**
     * @return 值是否存在
     */
    boolean isPresent();

    /**
     * @return 值是否不存在
     * @see #isPresent()
     */
    default boolean notPresent() {
        return !isPresent();
    }

    /**
     * @return 实现类的简称
     */
    String name();

    String toString();

}
