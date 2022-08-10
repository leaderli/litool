package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.instruct.Instruct;
import io.leaderli.litool.runner.xml.funcs.FuncElement;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;
import io.leaderli.litool.runner.xml.funcs.ParamElement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 将所有的funcElement转化为 {@code Function<Context, Object>},用于计算func结果
 */
public class FuncsElementExecutor extends BaseElementExecutor<FuncsElement> {


    public FuncsElementExecutor(FuncsElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        Map<String, Function<Context, Object>> funcResultMap = new HashMap<>();
        for (FuncElement funcElement : element.getFuncList().lira()) {

            String name = funcElement.getName();
            funcResultMap.put(name, cx -> {
                Instruct instruct = funcElement.getInstruct();

                Object[] params = funcElement.getParamList()
                        .lira()
                        .map(ParamElement::getExpression)
                        .map(context::getExpressionValue)
                        .toArray();

                return instruct.apply(params);

            });
        }
        context.setFuncResultMap(funcResultMap);
    }

}
