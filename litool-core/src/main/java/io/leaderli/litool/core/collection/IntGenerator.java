package io.leaderli.litool.core.collection;

/**
 * @author leaderli
 * @since 2022/8/31
 */
public class IntGenerator implements Generator<Integer> {
    int i = 0;

    @Override
    public Integer next() {
        return i++;
    }
}
