package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class EndEvent extends LiEventObject<Void> {


private static final EndEvent INSTANCE = new EndEvent();


private EndEvent() {
    super(null);
}

public static EndEvent of() {
    return INSTANCE;
}
}
