package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.funcs.FuncsElementExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FuncsElement extends SaxBean implements ElementExecutor<FuncsElement, FuncsElementExecutor> {

    public final FuncList funcList = new FuncList();

    public FuncsElement() {
        super("funcs");
    }

    public void addFunc(FuncElement funcElement) {
        funcList.add(funcElement);
    }

    @Override
    public void end_check(List<String> parseErrorMsgs) {

        Set<String> nameSet = new HashSet<>();
        Set<String> labelSet = new HashSet<>();
        for (FuncElement funcElement : funcList.lira()) {
            String id = funcElement.getId();
            id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
            List<String> map = funcElement.getParams().lira()
                    .map(ParamElement::getExpression)
                    .filter(expr -> expr.getModel() == VariablesModel.FUNC)
                    .map(Expression::getName)
                    .filter(paramName -> !nameSet.contains(paramName))
                    .map(paramName -> String.format("error reference [%s]:[%s] can only use func ref defined before " +
                            "this" +
                            ".", paramName, funcElement.getName())).get();
            SaxEventHandler.addErrorMsgs(parseErrorMsgs, map.isEmpty(), StringUtils.join("\n", map.toArray()) + id);

            SaxEventHandler.addErrorMsgs(parseErrorMsgs, nameSet.add(funcElement.getName()), String.format("duplicate" +
                    " " +
                    "name of %s%s", funcElement.getName(), id));
            SaxEventHandler.addErrorMsgs(parseErrorMsgs, labelSet.add(funcElement.getLabel()), String.format(
                    "duplicate " +
                            "label of %s%s ", funcElement.getLabel(), id));

        }
    }


    public FuncList getFuncList() {
        return funcList;
    }

    @Override
    public FuncsElementExecutor executor() {
        return new FuncsElementExecutor(this);
    }
}
