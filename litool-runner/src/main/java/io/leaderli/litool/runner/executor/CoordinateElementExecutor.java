package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;


public class CoordinateElementExecutor extends BaseElementExecutor<CoordinateElement>{
    public CoordinateElementExecutor(CoordinateElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        String x = (String) context.getExpressionValue(element.getX());
        String y = (String) context.getExpressionValue(element.getY());
//        raw.get(0).stream().skip(1)

        context.setTemp(TempNameEnum.coordinate.name(), "");
    }
}
