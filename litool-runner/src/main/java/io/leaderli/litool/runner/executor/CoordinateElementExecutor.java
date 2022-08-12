package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;
import io.leaderli.litool.runner.xml.router.task.TdElement;

import java.util.Arrays;
import java.util.List;


public class CoordinateElementExecutor extends BaseElementExecutor<CoordinateElement>{
    public CoordinateElementExecutor(CoordinateElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        Object x = context.getExpressionValue(element.getX());
        Object y = context.getExpressionValue(element.getY());
        //TODO
        List<List<String>> raw = element.getTdList().lira()
                .map(TdElement::getValue)
                .map(s -> Arrays.asList(StringUtils.split(",")))
                .getRaw();
//        raw.get(0).stream().skip(1)

        context.setTemp(TempNameEnum.coordinate.name(), "");
    }
}
