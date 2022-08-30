package io.leaderli.litool.core.collection;

import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/8/30
 */
public interface Generator<T> extends Iterator<T> {
@Override
default boolean hasNext() {
    return true;
}

}
