package io.leaderli.litool.core.lang3;

import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class LiCollectionUtils {
    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable == null) return false;
        Iterator<?> iterator = iterable.iterator();
        return iterator.hasNext();
    }
}
