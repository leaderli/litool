package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public interface LiValue {

    /**
     * @return value does not exist
     * @see #present()
     */
    default boolean absent() {
        return !present();
    }

    /**
     * @return whether the value exists
     */
    boolean present();

    /**
     * @return short name of the implementation class
     */
    String name();


}
