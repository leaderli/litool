package io.leaderli.litool.core.collection;

/**
 * an infinite auto-incrementing generator
 *
 * @author leaderli
 * @since 2022/8/31
 */
public class IntGenerator implements Generator<Integer> {

    private int i = 0;

    @SuppressWarnings("java:S2272")
    @Override
    public Integer next() {
        return i++;
    }

    @Override
    public String name() {
        return "IntGenerator";
    }
}
