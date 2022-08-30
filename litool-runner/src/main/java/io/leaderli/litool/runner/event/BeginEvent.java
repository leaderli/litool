package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class BeginEvent extends LiEventObject<Void> {


    private static final BeginEvent INSTANCE = new BeginEvent();

    private BeginEvent() {
        super(null);
    }

    public static BeginEvent of() {
        return INSTANCE;
    }
}
