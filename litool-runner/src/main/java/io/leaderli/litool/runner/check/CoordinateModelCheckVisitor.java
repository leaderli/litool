package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.TypeAlias;

public class CoordinateModelCheckVisitor extends ModelCheckVisitor {

    public CoordinateModelCheckVisitor(MainCheckVisitor mainCheckVisitor) {
        super(mainCheckVisitor);
    }

    @Override
    public void request(String name, String id) {
        mainElement().getRequest().entryList.lira()
                .first(entry -> StringUtils.equals(name, entry.getKey()))
                .ifPresent(entry -> addErrorMsgs(TypeAlias.getType(entry.getType()) == String.class,String.format("coordinate expression [%s] only support string %s", name, id)));
    }

    @Override
    public void response(String name, String id) {
        mainElement().getResponse().entryList.lira()
                .first(entry -> StringUtils.equals(name, entry.getKey()))
                .ifPresent(entry -> addErrorMsgs(TypeAlias.getType(entry.getType()) == String.class,String.format("coordinate expression [%s] only support string %s", name, id)));
    }

    @Override
    public void func(String name, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void temp(String name, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(String name, String id) {
        throw new UnsupportedOperationException();
    }
}
