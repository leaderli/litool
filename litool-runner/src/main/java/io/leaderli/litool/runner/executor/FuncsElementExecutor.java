package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.instruct.FuncScope;
import io.leaderli.litool.runner.instruct.IFunc;
import io.leaderli.litool.runner.xml.funcs.FuncElement;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;
import io.leaderli.litool.runner.xml.funcs.ParamElement;

import java.util.HashMap;
import java.util.Map;

/**
 * 将所有的funcElement转化为 {@code Function<Context, Object>},用于计算func结果
 */
public class FuncsElementExecutor extends BaseElementExecutor<FuncsElement> {

    public FuncsElementExecutor(FuncsElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        Map<String, IFunc> funcResultMap = new HashMap<>();

        for (FuncElement funcElement : element.getFuncList().lira()) {

            String name = funcElement.getName();

            FuncScope scope = FuncScope.CONSTANT;
            // instruct
            if (funcElement.getInstruct().getScope().level > scope.level) {
                scope = funcElement.getInstruct().getScope();
            }
            // param model scope
            Lino<FuncScope> first = funcElement.getParamList()
                    .lira()
                    .map(ParamElement::getExpression)
                    .map(expr -> {
                        if (expr.getModel() == VariablesModel.FUNC) {
                            return funcResultMap.get(expr.getName()).getFuncScope();
                        }
                        return expr.getModel().scope;
                    })
                    .sort((o1, o2) -> o2.level - o1.level)
                    .first();
            if (first.present()) {
                scope = first.get();
            }

            FuncScope finalScope = scope;
            IFunc iFunc = new IFunc(funcElement, scope);
            funcResultMap.put(name, iFunc);
        }

        context.setFuncContainer(ImmutableMap.of(funcResultMap));
    }

}
