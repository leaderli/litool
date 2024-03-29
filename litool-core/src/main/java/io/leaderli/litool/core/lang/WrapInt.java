package io.leaderli.litool.core.lang;

import java.util.Iterator;

/**
 * 用来表示一个环绕的int,其值为 min ~ max。当值等于max时，下一个值将会是min
 */
public class WrapInt implements Iterator<Integer>, Iterable<Integer> {
    /**
     * 最大值
     */
    public final int max;
    /**
     * 最小值
     */
    public final int min;
    private int index;

    /**
     * 最小值取0
     *
     * @param max 最大值
     */
    public WrapInt(int max) {
        this(0, max);
    }

    /**
     * @param max 最大值
     * @param min 最小值
     */
    public WrapInt(int min, int max) {
        this.min = min;
        this.max = max;
        this.index = min;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * @return 返回下一个值
     */
    public Integer next() {
        if (this.index == max) {
            this.index = min;
            return max;
        }
        return this.index++;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new WrapInt(min, max);
    }

    /**
     * @param index 设定一个index，将其转换为环绕内的数值
     */
    public void set(Integer index) {

        index = index % (max - min + 1);
        if (index < min) {
            index = (max - min + 1) + index;
        }
        this.index = index;
    }

    /**
     * @return {@link #index}
     */
    public Integer get() {
        return this.index;
    }
}
