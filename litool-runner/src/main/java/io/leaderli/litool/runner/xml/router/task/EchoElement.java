package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.executor.EchoElementExecutor;

public class EchoElement extends TaskElement<EchoElement, EchoElementExecutor> {

    private String msg;

    @Override
    public void body(BodyEvent bodyEvent) {
        this.msg = bodyEvent.description();
    }

    @Override
    public String tag() {
        return "echo";
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public EchoElementExecutor executor() {
        return new EchoElementExecutor(this);
    }
}
