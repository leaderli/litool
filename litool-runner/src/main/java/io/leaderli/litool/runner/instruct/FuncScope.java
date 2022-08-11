package io.leaderli.litool.runner.instruct;

public enum FuncScope {

    CONSTANT(0),
    CONTEXT(1),
    RUNTIME(2);

    FuncScope(int level) {
        this.level = level;
    }

    public final int level;

    public static FuncScope getFuncScope(int level) {
        for (FuncScope funcScope : values()) {
            if (funcScope.level == level) {
                return funcScope;
            }
        }
        return RUNTIME;
    }

}
