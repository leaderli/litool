package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxBean;

public class UnitElement implements SaxBean {

    private String label;

    private IfList ifList;

    public void addCondition(IfElement ifElement) {
        ifList.add(ifElement);
    }

    @Override
    public String name() {
        return "unit";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
