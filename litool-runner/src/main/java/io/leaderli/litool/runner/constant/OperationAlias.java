package io.leaderli.litool.runner.constant;

import java.util.HashMap;
import java.util.Map;

//TODO 改成枚举，以及参数校验
public class OperationAlias {
    public static final Map<String, String> OP_ALIAS = new HashMap<>();

    static {
        OP_ALIAS.put(">", ">");
        OP_ALIAS.put("gt", ">");
        OP_ALIAS.put("大于", ">");
        OP_ALIAS.put(">=", ">=");
        OP_ALIAS.put("ge", ">=");
        OP_ALIAS.put("大于等于", ">=");
        OP_ALIAS.put("<", "<");
        OP_ALIAS.put("lt", "<");
        OP_ALIAS.put("小于", "<");
        OP_ALIAS.put("<=", "<=");
        OP_ALIAS.put("le", "<=");
        OP_ALIAS.put("小于等于", "<=");
        OP_ALIAS.put("=", "=");
        OP_ALIAS.put("e", "=");
        OP_ALIAS.put("等于", "=");
    }

    public static String getOperation(String op) {
        return OP_ALIAS.getOrDefault(op, op);
    }
}
