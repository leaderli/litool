package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;

public class SequenceElement implements SaxBean {

    private String label;

    private UnitList unitList;

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(unitList.lira().size() == 0, "the unitList of sequence is empty");

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "sequence";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UnitList getUnitList() {
        return unitList;
    }

    public void setUnitList(UnitList unitList) {
        this.unitList = unitList;
    }
}
