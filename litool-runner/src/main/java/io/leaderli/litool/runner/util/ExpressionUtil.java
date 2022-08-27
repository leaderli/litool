package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;

public class ExpressionUtil {

public static final Expression ERROR2 = new Expression("", VariablesModel.ERROR);
public static final String REQUEST_PREFIX = "$";
public static final String RESPONSE_PREFIX = "@";
public static final String TEMP_PREFIX = "#";
public static final String FUNC_SUFFIX = "()";

public static Expression getExpression(String expr) {
    if (expr == null) {
        return ERROR2;
    }
    if (expr.startsWith(REQUEST_PREFIX)) {
        return new Expression(StringUtils.removeStart(expr, REQUEST_PREFIX), VariablesModel.REQUEST);
    }
    if (expr.startsWith(RESPONSE_PREFIX)) {
        return new Expression(StringUtils.removeStart(expr, RESPONSE_PREFIX), VariablesModel.RESPONSE);
    }
    if (expr.startsWith(TEMP_PREFIX)) {
        return new Expression(StringUtils.removeStart(expr, TEMP_PREFIX), VariablesModel.TEMP);
    }
    if (expr.endsWith(FUNC_SUFFIX)) {
        return new Expression(StringUtils.removeEnd(expr, FUNC_SUFFIX), VariablesModel.FUNC);
    }
    return new Expression(expr, VariablesModel.LITERAL);
}

}
