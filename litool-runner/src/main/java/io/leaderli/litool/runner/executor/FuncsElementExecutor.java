package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.InnerFuncContainer;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FuncsElementExecutor extends BaseElementExecutor<FuncsElement> {


    public FuncsElementExecutor(FuncsElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        Map<String, Function<Context, Object>> funcResult = new HashMap<>();
        element.getFuncList().lira().forEach(funcElement -> {
            String name = funcElement.getName();
            String clazz = funcElement.getInstruct();
            funcResult.put(name, context1 -> {
                Method method = InnerFuncContainer.getInnerMethodByAlias(clazz);
                List<Object> params = new ArrayList<>();
                funcElement.getParamList().lira().forEach(paramElement -> {

                });
                try {
                    return method.invoke(null, params.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

}
