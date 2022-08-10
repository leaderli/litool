package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.constant.VariablesModel;

public class ExpressionUtil {

    public static final LiTuple2<String, VariablesModel> ERROR = new LiTuple2<>("", VariablesModel.ERROR);
    public static final String REQUEST_PREFIX = "$";
    public static final String RESPONSE_PREFIX = "@";
    public static final String TEMP_PREFIX = "#";
    public static final String FUNC_SUFFIX = "()";

    public static LiTuple2<String, VariablesModel> getExpression(String expr) {
        if (StringUtils.isBlank(expr)) {
            return ERROR;
        }
        if (expr.startsWith(REQUEST_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, REQUEST_PREFIX), VariablesModel.REQUEST);
        }
        if (expr.startsWith(RESPONSE_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, RESPONSE_PREFIX), VariablesModel.RESPONSE);
        }
        if (expr.startsWith(TEMP_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, TEMP_PREFIX), VariablesModel.TEMP);
        }
        if (expr.endsWith(FUNC_SUFFIX)) {
            return new LiTuple2<>(StringUtils.removeEnd(expr, FUNC_SUFFIX), VariablesModel.FUNC);
        }
        return new LiTuple2<>(expr, VariablesModel.LITERAL);
    }

}
