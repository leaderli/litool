package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.RouterElementExecutor;

import java.util.HashSet;
import java.util.Set;

public class RouterElement implements SaxBean, ElementExecutor<RouterElement, RouterElementExecutor> {

    private SequenceList sequenceList;

    public void addSequence(SequenceElement sequenceElement) {
        sequenceList.add(sequenceElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(sequenceList.lira().size() == 0, "the sequenceList of route is empty");

        Set<String> nameSet = new HashSet<>();
        Set<String> labelSet = new HashSet<>();
        for (SequenceElement sequenceElement : sequenceList.lira()) {
            LiAssertUtil.assertTrue(nameSet.add(sequenceElement.getName()), "duplicate name of " + sequenceElement.getName());
            LiAssertUtil.assertTrue(labelSet.add(sequenceElement.getLabel()), "duplicate label of " + sequenceElement.getLabel());
        }

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "router";
    }

    public SequenceList getSequenceList() {
        return sequenceList;
    }

    public void setSequenceList(SequenceList sequenceList) {
        this.sequenceList = sequenceList;
    }

    @Override
    public RouterElementExecutor executor() {
        return new RouterElementExecutor(this);
    }
}
