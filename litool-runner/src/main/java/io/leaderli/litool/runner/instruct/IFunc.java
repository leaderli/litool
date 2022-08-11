package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.funcs.FuncElement;
import io.leaderli.litool.runner.xml.funcs.ParamElement;

public class IFunc {

    private Object cache;

    public final String name;
    public final FuncElement funcElement;
    public final FuncScope funcScope;

    public IFunc(FuncElement funcElement, FuncScope funcScope) {
        this.name = funcElement.getName();
        this.funcElement = funcElement;
        this.funcScope = funcScope;
    }

    public Object apply(Context context) {
        switch (funcScope) {
            case CONTEXT:
                Object funcResult = context.getFuncResultCache(name);
                if (funcResult == null) {
                    funcResult = directInvoke(context);
                    context.setFuncResultCache(name, funcResult);
                }
                return funcResult;
            case CONSTANT:
                if (cache == null) {
                    cache = directInvoke(context);
                }
                return cache;
            default:
                return directInvoke(context);
        }
    }

    public FuncScope getFuncScope() {
        return funcScope;
    }

    public Object directInvoke(Context context) {
        Object[] params = funcElement.getParamList()
                .lira()
                .map(ParamElement::getExpression)
                .map(context::getExpressionValue)
                .toArray();
        return funcElement.getInstruct().apply(params);
    }

}
