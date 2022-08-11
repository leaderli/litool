package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionUtilTest {

    @Test
    void getExpression_blank() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("");
        Assertions.assertSame(expression, ExpressionUtil.ERROR);

    }

    @Test
    void getExpression_literal() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("123");
        Assertions.assertEquals(expression._1, "123");
        Assertions.assertSame(expression._2, VariablesModel.LITERAL);
    }

    @Test
    void getExpression_request() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("$123");
        Assertions.assertEquals(expression._1, "123");
        Assertions.assertSame(expression._2, VariablesModel.REQUEST);
    }

    @Test
    void getExpression_response() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("@123");
        Assertions.assertEquals(expression._1, "123");
        Assertions.assertSame(expression._2, VariablesModel.RESPONSE);
    }

    @Test
    void getExpression_temp() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("#123");
        Assertions.assertEquals(expression._1, "123");
        Assertions.assertSame(expression._2, VariablesModel.TEMP);
    }

    @Test
    void getExpression_func() {

        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("123()");
        Assertions.assertEquals(expression._1, "123");
        Assertions.assertSame(expression._2, VariablesModel.FUNC);
    }
}
