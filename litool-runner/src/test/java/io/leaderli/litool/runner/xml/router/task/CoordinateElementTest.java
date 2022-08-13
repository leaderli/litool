package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


class CoordinateElementTest {

    @Test
    void success() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate.xml");

        Context context = new Context(new HashMap<>());
        coordinateElement.executor().visit(context);
        Object value = context.getTemp(TempNameEnum.coordinate.name());
        Assertions.assertEquals(value, "3");
    }

    @Test
    void def_success() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_def.xml");

        Context context = new Context(new HashMap<>());
        coordinateElement.executor().visit(context);
        Object value = context.getTemp(TempNameEnum.coordinate.name());
        Assertions.assertEquals(value, "1");
    }
    @Test
    void error() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_error.xml");

        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "coordinate td should have same size "));
    }

    @Test
    void td_error() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_td_error.xml");

        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "the td value ,1,2 is not match"));
        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(2), "the td value 2, is not match"));
    }

    @Test
    void td_size_error() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_td_size_error.xml");

        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "coordinate td should have two or more"));
    }

    @Test
    void x_y_duplicate_error() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_x_y_duplicate_error.xml");

        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "x-coordinate should not repeated"));
        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(1), "y-coordinate should not repeated"));
    }
}
