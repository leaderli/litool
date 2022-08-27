package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.runner.event.KafkaEvent;
import io.leaderli.litool.runner.xml.router.task.KafkaElement;

public class KafkaElementExecutor extends BaseEventElementExecutor<KafkaElement, KafkaEvent> {
public KafkaElementExecutor(KafkaElement element) {
    super(element);
}

@Override
public KafkaEvent newEvent(String message) {
    return new KafkaEvent(element.getTopic(), message);
}


}
