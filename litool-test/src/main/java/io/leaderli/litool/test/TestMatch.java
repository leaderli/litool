package io.leaderli.litool.test;

/**
 * @author leaderli
 * @since 2022/9/30 5:21 PM
 */
public interface TestMatch<T> {


    default boolean match(T t) {
        return true;
    }

    boolean test(T t);

}
