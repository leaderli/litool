package io.leaderli.litool.core.collection;

import java.util.List;

public interface ToList<T, L extends List<T>> {
    /**
     * @return 返回一个包含所有元素的 {@link L}
     */
    L toList();
}
