package io.leaderli.litool.runner.util;

import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionUtilTest {

    @Test
    void getExpression_blank() {

        Expression expression = ExpressionUtil.getExpression(null);
        Assertions.assertSame(VariablesModel.ERROR, expression.getModel());

        expression = ExpressionUtil.getExpression("");
        Assertions.assertSame(VariablesModel.LITERAL, expression.getModel());

    }

    @Test
    void getExpression_literal() {

        Expression expression = ExpressionUtil.getExpression("123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(VariablesModel.LITERAL, expression.getModel());
    }

    @Test
    void getExpression_request() {

        Expression expression = ExpressionUtil.getExpression("$123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(VariablesModel.REQUEST, expression.getModel());
    }

    @Test
    void getExpression_response() {

        Expression expression = ExpressionUtil.getExpression("@123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(VariablesModel.RESPONSE, expression.getModel());
    }

    @Test
    void getExpression_temp() {

        Expression expression = ExpressionUtil.getExpression("#123");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(VariablesModel.TEMP, expression.getModel());
    }

    @Test
    void getExpression_func() {

        Expression expression = ExpressionUtil.getExpression("123()");
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(VariablesModel.FUNC, expression.getModel());
    }
}
