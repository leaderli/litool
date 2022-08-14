package io.leaderli.litool.runner.util;

import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionUtilTest {

    @Test
    void getExpression_blank() {

        Expression  expression = ExpressionUtil.getExpression(null);
        Assertions.assertSame(expression.getModel(), VariablesModel.ERROR);

        expression = ExpressionUtil.getExpression("");
        Assertions.assertSame(expression.getModel(), VariablesModel.LITERAL);

    }

    @Test
    void getExpression_literal() {

        Expression expression = ExpressionUtil.getExpression("123");
        Assertions.assertEquals(expression.getName(), "123");
        Assertions.assertSame(expression.getModel(), VariablesModel.LITERAL);
    }

    @Test
    void getExpression_request() {

        Expression expression = ExpressionUtil.getExpression("$123");
        Assertions.assertEquals(expression.getName(), "123");
        Assertions.assertSame(expression.getModel(), VariablesModel.REQUEST);
    }

    @Test
    void getExpression_response() {

        Expression expression = ExpressionUtil.getExpression("@123");
        Assertions.assertEquals(expression.getName(), "123");
        Assertions.assertSame(expression.getModel(), VariablesModel.RESPONSE);
    }

    @Test
    void getExpression_temp() {

        Expression expression = ExpressionUtil.getExpression("#123");
        Assertions.assertEquals(expression.getName(), "123");
        Assertions.assertSame(expression.getModel(), VariablesModel.TEMP);
    }

    @Test
    void getExpression_func() {

        Expression expression = ExpressionUtil.getExpression("123()");
        Assertions.assertEquals(expression.getName(), "123");
        Assertions.assertSame(expression.getModel(), VariablesModel.FUNC);
    }
}
