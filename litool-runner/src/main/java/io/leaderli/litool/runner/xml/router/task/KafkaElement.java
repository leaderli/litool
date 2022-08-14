package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.runner.event.KafkaEvent;
import io.leaderli.litool.runner.executor.router.task.KafkaElementExecutor;

public class KafkaElement extends BaseEventElement<KafkaElement, KafkaElementExecutor, KafkaEvent> {

    private String topic = "";


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    @Override
    public String tag() {
        return "echo";
    }


}
