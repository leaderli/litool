package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public final class KafkaEvent extends LiEventObject<LiTuple2<String, String>> {


public KafkaEvent(String topic, String source) {
    super(LiTuple.of(topic, source));
}
}
