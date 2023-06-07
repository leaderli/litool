package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.meta.LiTuple;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class KafkaEvent extends LiEventObject<LiTuple<String, String>> {


    public KafkaEvent(String topic, String source) {
        super(LiTuple.of(topic, source));
    }
}
