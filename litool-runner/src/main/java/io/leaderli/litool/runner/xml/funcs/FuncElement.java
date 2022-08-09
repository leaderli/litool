package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.FuncClassContainer;
import io.leaderli.litool.runner.TypeAlias;

public class FuncElement implements SaxBean {

    private String label;
    private String name;
    private String clazz;
    private String type;

    private ParamList paramList;

    public void addParam(ParamElement paramElement) {
        paramList.add(paramElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        FuncClassContainer.funcElementCheck(this);

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "func";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        LiAssertUtil.assertTrue(name.matches(LiConstant.ENTRY_NAME_RULE), String.format("the func name [%s] is not match %s", name, LiConstant.ENTRY_NAME_RULE));
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        LiAssertUtil.assertTrue(TypeAlias.ALIAS.containsKey(type), String.format("the func type [%s] is unsupported", type));

        this.type = type;
    }

    public ParamList getParamList() {
        return paramList;
    }

    public void setParamList(ParamList paramList) {
        this.paramList = paramList;
    }
}
