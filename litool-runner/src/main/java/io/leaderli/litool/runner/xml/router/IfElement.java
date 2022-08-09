package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxBean;

public class IfElement implements SaxBean {

    private String cond;



    @Override
    public String name() {
        return "if";
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        // TODO cond校验

        this.cond = cond;
    }
}
