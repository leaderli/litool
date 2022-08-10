package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 将所有的funcElement转化为Function<Context, Object>,用于计算func结果
 */
public class FuncsElementExecutor extends BaseElementExecutor<FuncsElement> {


    public FuncsElementExecutor(FuncsElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        Map<String, Function<Context, Object>> funcResultMap = new HashMap<>();
        element.getFuncList().lira().forEach(funcElement -> {
            String name = funcElement.getName();
            String clazz = funcElement.getInstruct();
            funcResultMap.put(name, inContext -> {
                Method method = InstructContainer.getInnerMethodByAlias(clazz);
                List<Object> params = new ArrayList<>();
                funcElement.getParamList().lira().forEach(paramElement -> {
                    Expression expression = paramElement.getExpression();
                    Object param = expression.getModel().apply(inContext, expression.getName());
                    params.add(param);
                });
                try {
                    return method.invoke(null, params.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        context.setFuncResultMap(funcResultMap);
    }

}
