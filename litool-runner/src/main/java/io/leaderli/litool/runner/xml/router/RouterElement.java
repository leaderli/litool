package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.RouterElementExecutor;

import java.util.HashSet;
import java.util.Set;

public class RouterElement implements SaxBean, ElementExecutor<RouterElement, RouterElementExecutor> {

    private SequenceList sequenceList = new SequenceList();

    public void addSequence(SequenceElement sequenceElement) {
        sequenceList.add(sequenceElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        SaxBean.super.end(endEvent);

        LiAssertUtil.assertFalse(sequenceList.lira().size() == 0, "the sequenceList of route is empty");

        Set<String> nameSet = new HashSet<>();
        Set<String> labelSet = new HashSet<>();
        for (SequenceElement sequenceElement : sequenceList.lira()) {
            LiAssertUtil.assertTrue(nameSet.add(sequenceElement.getName()), "duplicate name of " + sequenceElement.getName());
            LiAssertUtil.assertTrue(labelSet.add(sequenceElement.getLabel()), "duplicate label of " + sequenceElement.getLabel());
        }
    }

    @Override
    public String tag() {
        return "router";
    }

    public SequenceList getSequenceList() {
        return sequenceList;
    }

    public void setSequenceList(SequenceList sequenceList) {
        this.sequenceList = sequenceList;
    }

}
