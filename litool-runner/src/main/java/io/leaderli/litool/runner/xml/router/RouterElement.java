package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;

public class RouterElement implements SaxBean {

    private SequenceList sequenceList;

    public void addSequence(SequenceElement sequenceElement) {
        sequenceList.add(sequenceElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(sequenceList.lira().size() == 0, "the sequenceList of route is empty");

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "route";
    }

    public SequenceList getSequenceList() {
        return sequenceList;
    }

    public void setSequenceList(SequenceList sequenceList) {
        this.sequenceList = sequenceList;
    }
}
