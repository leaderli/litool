package io.leaderli.litool.core.meta;

/**
 * 代表一个值的接口，提供了判断值是否存在、获取实现类短名称等方法。
 */
public interface LiValue {

    /**
     * 判断该值是否不存在。
     *
     * @return 如果值不存在，则返回 true；否则返回 false
     * @see #present()
     */


    default boolean absent() {
        return !present();
    }

    /**
     * 判断该值是否存在。
     *
     * @return 如果值存在，则返回 true；否则返回 false
     */
    boolean present();

    /**
     * 获取实现类的短名称。
     *
     * @return 实现类的短名称
     */
    String name();


}
