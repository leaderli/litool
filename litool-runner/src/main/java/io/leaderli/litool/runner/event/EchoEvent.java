package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class EchoEvent extends LiEventObject<String[]> {

    public EchoEvent(String level, String source) {
        super(new String[]{level, source});
    }
}
