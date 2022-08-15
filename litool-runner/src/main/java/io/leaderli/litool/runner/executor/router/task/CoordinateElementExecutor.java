package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;
import io.leaderli.litool.runner.xml.router.task.TdElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CoordinateElementExecutor extends BaseElementExecutor<CoordinateElement> {
    private ImmutableMap<String ,Map<String,String>> coordinateMap;
    private String def;
    public CoordinateElementExecutor(CoordinateElement element) {
        super(element);
        init();
    }

    private void init () {
        Lira<List<String>> tdLira = element.getTdList().lira()
                .map(TdElement::getValue);
        Lira<String> firstLine = tdLira.first().toLira(String.class);
        def = firstLine.first().get();
        List<String> x_axis = firstLine.skip(1).getRaw();
        Map<String,Map<String,String>> map = new HashMap<>();
        for (List<String> line : tdLira.skip(1)) {
            HashMap<String, String> xMap = new HashMap<>();
            map.put(line.get(0),xMap );
            for (int i = 0; i < x_axis.size(); i++) {
                String current_x = x_axis.get(i);
                String current_y = line.get(i + 1);
                xMap.put(current_x, current_y);
            }
        }
        coordinateMap = ImmutableMap.of(map);
    }
    @Override
    public void execute(Context context) {
        String x = (String) context.getExpressionValue(element.getX());
        String y = (String) context.getExpressionValue(element.getY());
        context.setTemp(TempNameEnum.coordinate.name(), Lino.of(coordinateMap.get(y)).map(m -> m.get(x)).get(def));
    }
}
