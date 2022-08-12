package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CoordinateElementTest {

    @Test
    void success() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate.xml");

        Assertions.assertTrue(dfs.getParseErrorMsgs().size() == 0);
    }

    @Test
    void error() {
        SaxEventInterceptor<CoordinateElement> dfs = new SaxEventInterceptor<>(CoordinateElement.class);

        CoordinateElement coordinateElement = dfs.parse("router/task/coordinate_error.xml");

        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "coordinate td should have same size "));
    }


}
