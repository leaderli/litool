package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.FuncsElementExecutor;

import java.util.Objects;

public class FuncsElement implements SaxBean, ElementExecutor<FuncsElementExecutor> {

    private FuncList funcList = new FuncList();

    public void addFunc(FuncElement funcElement) {
        funcList.lira().iterator().forEachRemaining(entry -> {
            LiAssertUtil.assertFalse(Objects.equals(entry.getName(), funcElement.getName()), "duplicate name of " + funcElement.getName());
            LiAssertUtil.assertFalse(Objects.equals(entry.getLabel(), funcElement.getLabel()), "duplicate label of " + funcElement.getLabel());
        });

        funcList.add(funcElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(funcList.lira().size() == 0, "the funcList of funcs is empty");

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "funcs";
    }

    public FuncList getFuncList() {
        return funcList;
    }

    public void setFuncList(FuncList funcList) {
        this.funcList = funcList;
    }

    @Override
    public FuncsElementExecutor executor() {
        return new FuncsElementExecutor(this);
    }
}
