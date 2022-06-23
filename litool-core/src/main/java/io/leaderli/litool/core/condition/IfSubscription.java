package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;

public interface IfSubscription<T> {

    /**
     * 请求数据
     */
    void request(LiBox<T> box);

    /**
     * 取消操作
     */
    void onComplete(T value);
}
