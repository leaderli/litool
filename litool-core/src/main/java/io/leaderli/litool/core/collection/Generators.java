package io.leaderli.litool.core.collection;

/**
 * @author leaderli
 * @since 2022/8/31
 */
public class Generators {

    public static Generator<Integer> range() {
        return new IntGenerator();
    }
}
