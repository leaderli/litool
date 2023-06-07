package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.meta.LiTuple;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class EchoEvent extends LiEventObject<LiTuple<Integer, String>> {

    public static final int ERROR = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;

    public EchoEvent(int level, String source) {
        super(LiTuple.of(level, source));
    }
}
