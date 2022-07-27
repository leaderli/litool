package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class NoBean implements SaxBean {
    public String name;

    public String body;

    @Override
    public void body(BodyEvent bodyEvent) {
        this.body = bodyEvent.description();
    }
}
