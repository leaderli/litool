package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

public class IFunc {

    public final String name;
    public final FuncElement funcElement;
    public final FuncScope funcScope;
    private Object cache;

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

    public Object directInvoke(Context context) {
        Object[] params = funcElement.getParams()
                .lira()
                .map(param -> {
                    Expression expression = param.getExpression();
                    Object result = expression.apply(context);

                    if (expression.getModel() == VariablesModel.LITERAL) {
                        return TypeAlias.parser(param.getType(), (String) result);
                    }
                    return result;

                })
                .toArray();
        return funcElement.getInstruct().apply(TypeAlias.getType(funcElement.getType()), params);
    }

    public FuncScope getFuncScope() {
        return funcScope;
    }

}
