package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiValue {

    /**
     * @return 值是否存在
     */
    boolean present();

    /**
     * @return 值是否不存在
     * @see #present()
     */
    default boolean absent() {
        return !present();
    }

    /**
     * @return 实现类的简称
     */
    String name();

    String toString();

}
