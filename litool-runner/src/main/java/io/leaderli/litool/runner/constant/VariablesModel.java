package io.leaderli.litool.runner.constant;

import io.leaderli.litool.runner.Context;

import java.util.function.BiFunction;

public enum VariablesModel implements BiFunction<Context, String, Object> {

    ERROR(0) {
        @Override
        public Object apply(Context context, String ignore) {
            throw new UnsupportedOperationException("ERROR VariablesModel");
        }
    },
    LITERAL(1) {
        @Override
        public Object apply(Context context, String value) {
            return value;
        }
    },
    REQUEST(2) {
        @Override
        public Object apply(Context context, String name) {
            return context.getRequest(name);
        }
    },
    RESPONSE(3) {
        @Override
        public Object apply(Context context, String name) {
            return context.getResponse(name);
        }
    },
    TEMP(4) {
        @Override
        public Object apply(Context context, String name) {
            return context.getTemp(name);
        }
    },
    FUNC(5) {
        @Override
        public Object apply(Context context, String name) {
            // TODO 方法取值逻辑
            return null;
        }
    };

    final int modelType;

    VariablesModel(int modelType) {
        this.modelType = modelType;
    }

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
