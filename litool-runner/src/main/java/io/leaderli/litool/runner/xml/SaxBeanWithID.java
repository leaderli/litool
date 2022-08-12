package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;

public abstract class SaxBeanWithID implements SaxBean {


    protected String id = "";

    @Override
    public String id() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
