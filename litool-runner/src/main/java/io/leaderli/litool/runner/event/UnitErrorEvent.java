package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.meta.LiTuple;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class UnitErrorEvent extends LiEventObject<LiTuple<String, Throwable>> {

    public UnitErrorEvent(String id, Throwable source) {
        super(LiTuple.of(id, source));
    }
}
