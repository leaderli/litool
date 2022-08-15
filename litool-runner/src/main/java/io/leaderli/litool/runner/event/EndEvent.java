package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class EndEvent extends LiEventObject<Void> {


    private EndEvent() {
        super(null);
    }


    private final static EndEvent INSTANCE = new EndEvent();


    public static EndEvent of() {
        return INSTANCE;
    }
}
