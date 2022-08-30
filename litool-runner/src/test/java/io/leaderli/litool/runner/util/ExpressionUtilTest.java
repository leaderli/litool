package io.leaderli.litool.runner.util;

import io.leaderli.litool.runner.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.leaderli.litool.runner.constant.VariablesModel.*;

class ExpressionUtilTest {

    @Test
    void getExpression_blank() {

        Expression expression = ExpressionUtil.getExpression(null);
        Assertions.assertSame(ERROR, expression.getModel());

        expression = ExpressionUtil.getExpression("");
        Assertions.assertSame(LITERAL, expression.getModel());

    }

    @Test
    void getExpression_literal() {

        Expression expression = ExpressionUtil.getExpression("123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(LITERAL, expression.getModel());
    }

    @Test
    void getExpression_request() {

        Expression expression = ExpressionUtil.getExpression("$123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(REQUEST, expression.getModel());
    }

    @Test
    void getExpression_response() {

        Expression expression = ExpressionUtil.getExpression("@123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(RESPONSE, expression.getModel());
    }

    @Test
    void getExpression_temp() {

        Expression expression = ExpressionUtil.getExpression("#123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(TEMP, expression.getModel());
    }

    @Test
    void getExpression_func() {

        Expression expression = ExpressionUtil.getExpression("123()");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(FUNC, expression.getModel());
    }
}
