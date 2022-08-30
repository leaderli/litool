package io.leaderli.litool.runner.executor.funcs;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
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
    private final Map<String, IFunc> funcFactory = new HashMap<>();

    public FuncsElementExecutor(FuncsElement element) {
        super(element);
        init();
    }

    private void init() {


        for (FuncElement funcElement : element.funcList.lira()) {

            String name = funcElement.getName();

            FuncScope scope = FuncScope.CONSTANT;
            // instruct
            if (funcElement.getInstruct().getScope().level > scope.level) {
                scope = funcElement.getInstruct().getScope();
            }
            // param model scope
            Lino<FuncScope> first = funcElement.getParams()
                    .lira()
                    .map(ParamElement::getExpression)
                    .map(expr -> {
                        if (expr.getModel() == VariablesModel.FUNC) {
                            return funcFactory.get(expr.getName()).getFuncScope();
                        }
                        return expr.getModel().scope;
                    })
                    .sorted((o1, o2) -> o2.level - o1.level)
                    .first();
            if (first.present()) {
                scope = first.get().level > scope.level ? first.get() : scope;
            }


            IFunc iFunc = new IFunc(funcElement, scope);
            funcFactory.put(name, iFunc);
        }
    }

    @Override
    public void execute(Context context) {

        context.setFuncFactory(ImmutableMap.of(funcFactory));
    }

}
