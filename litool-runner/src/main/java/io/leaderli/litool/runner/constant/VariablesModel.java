package io.leaderli.litool.runner.constant;

import io.leaderli.litool.runner.Context;

import java.util.function.BiFunction;

public enum VariablesModel implements BiFunction<Context, String, Object> {

    ERROR(0) {
        @Override
        public Object apply(Context context, String s) {
            return null;
        }
    },
    LITERAL(1) {
        @Override
        public Object apply(Context context, String s) {
            return s;
        }
    },
    REQUEST(2) {
        @Override
        public Object apply(Context context, String s) {
            return context.getRequest(s);
        }
    },
    RESPONSE(3) {
        @Override
        public Object apply(Context context, String s) {
            return context.getResponse(s);
        }
    },
    TEMP(4) {
        @Override
        public Object apply(Context context, String s) {
            return null;
        }
    },
    FUNC(5) {
        @Override
        public Object apply(Context context, String s) {
            return null;
        }
    };

    VariablesModel(int modelType) {
        this.modelType = modelType;
    }

    final int modelType;

    public static VariablesModel getVariableModel(int modelType) {
        for (VariablesModel model : values()) {
            if (model.modelType == modelType) {
                return model;
            }
        }
        return LITERAL;
    }

    public int getModelType() {
        return modelType;
    }
}
