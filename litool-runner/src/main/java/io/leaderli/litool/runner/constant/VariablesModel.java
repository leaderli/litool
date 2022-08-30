package io.leaderli.litool.runner.constant;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.instruct.FuncScope;

import java.util.function.BiFunction;

public enum VariablesModel implements BiFunction<Context, Object, Object> {

    ERROR(0, FuncScope.CONSTANT) {
        @Override
        public Object apply(Context context, Object ignore) {
            throw new UnsupportedOperationException("ERROR VariablesModel");
        }
    },
    LITERAL(1, FuncScope.CONSTANT) {
        @Override
        public Object apply(Context context, Object value) {
            return value;
        }
    },
    REQUEST(2, FuncScope.CONTEXT) {
        @Override
        public Object apply(Context context, Object name) {
            return context.getRequest((String) name);
        }
    },
    RESPONSE(3, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, Object name) {
            return context.getResponse((String) name);
        }
    },
    TEMP(4, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, Object name) {
            return context.getTemp((String) name);
        }
    },
    FUNC(5, FuncScope.RUNTIME) {
        @Override
        public Object apply(Context context, Object name) {
            return context.getFuncResult((String) name);
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

    public boolean matchAny(VariablesModel... models) {
        for (VariablesModel model : models) {
            if (this == model) {
                return true;
            }
        }
        return false;
    }

}
