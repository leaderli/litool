package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.SequenceElementExecutor;

import java.util.HashSet;
import java.util.Set;

public class SequenceElement implements SaxBean, ElementExecutor<SequenceElement, SequenceElementExecutor> {

    private String name;
    private String label;

    private UnitList unitList = new UnitList();

    public void addUnit(UnitElement unitElement) {
        unitList.add(unitElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        SaxBean.super.end(endEvent);

        LiAssertUtil.assertFalse(unitList.lira().size() == 0, "the unitList of sequence is empty");

        Set<String> labelSet = new HashSet<>();
        for (UnitElement unitElement : unitList.lira()) {
            LiAssertUtil.assertTrue(labelSet.add(unitElement.getLabel()), "duplicate label of " + unitElement.getLabel());
        }
    }

    @Override
    public String tag() {
        return "sequence";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
