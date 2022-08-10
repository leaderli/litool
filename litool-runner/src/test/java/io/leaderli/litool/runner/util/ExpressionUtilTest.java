package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionUtilTest {

    @Test
    void getExpression_blank() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, ""));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.ERROR);
    }

    @Test
    void getExpression_literal() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("123");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, "123"));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.LITERAL);
    }

    @Test
    void getExpression_request() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("$123");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, "123"));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.REQUEST);
    }

    @Test
    void getExpression_response() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("@123");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, "123"));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.RESPONSE);
    }

    @Test
    void getExpression_temp() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("#123");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, "123"));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.TEMP);
    }

    @Test
    void getExpression_func() {
        LiTuple2<String, VariablesModel> expression = ExpressionUtil.getExpression("123()");

        System.out.println(expression);

        LiAssertUtil.assertTrue(StringUtils.equals(expression._1, "123"));
        LiAssertUtil.assertTrue(expression._2 == VariablesModel.FUNC);
    }
}