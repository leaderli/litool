package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2023/6/19 1:11 PM
 */
public class RangeIterator implements Iterator<Integer> {
    private final int start;
    private final int end;

    private int current;

    public RangeIterator(int start, int end) {
        this.start = start;
        this.end = end;
        this.current = start;
        LiAssertUtil.assertTrue(end > start);
    }

    @Override
    public boolean hasNext() {
        return current >= start && current < end;
    }

    @Override
    public Integer next() {
        if (hasNext()) {
            return current++;
        }
        throw new NoSuchElementException();
    }
}
