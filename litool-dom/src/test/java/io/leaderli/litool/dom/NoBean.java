package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class NoBean extends SaxBean {
    public String body;
    private String name;

    public NoBean() {
        super("nobean");
    }

    @Override
    public void setBody(BodyEvent bodyEvent) {
        this.body = bodyEvent.description();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
