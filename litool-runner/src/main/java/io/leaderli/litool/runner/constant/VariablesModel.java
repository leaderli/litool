package io.leaderli.litool.runner.constant;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.instruct.FuncScope;

import java.util.function.BiFunction;

public enum VariablesModel implements BiFunction<Context, String, Object> {

    ERROR(0, FuncScope.CONSTANT) {
        @Override
        public Object apply(Context context, String ignore) {
            throw new UnsupportedOperationException("ERROR VariablesModel");
        }
    },
    LITERAL(1, FuncScope.CONSTANT) {
        @Override
        public Object apply(Context context, String value) {
            return value;
        }
    },
    REQUEST(2, FuncScope.CONTEXT) {
        @Override
        public Object apply(Context context, String name) {
            return context.getRequest(name);
        }
    },
    RESPONSE(3, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, String name) {
            return context.getResponse(name);
        }
    },
    TEMP(4, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, String name) {
            return context.getTemp(name);
        }
    },
    FUNC(5, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, String name) {
            return context.getFuncResult(name);
        }
    };

    public final int type;
    public final FuncScope scope;

    VariablesModel(int type, FuncScope scope) {
        this.type = type;
        this.scope = scope;
    }

    public static VariablesModel getVariableModel(int modelType) {
        for (VariablesModel model : values()) {
            if (model.type == modelType) {
                return model;
            }
        }
        return LITERAL;
    }

}
