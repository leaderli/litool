package io.leaderli.litool.runner.instruct;

public enum FuncScope {

    CONSTANT(0),
    CONTEXT(1),
    RUNTIME(2);

    public final int level;

    FuncScope(int level) {
        this.level = level;
    }

    public static FuncScope getFuncScope(int level) {
        for (FuncScope funcScope : values()) {
            if (funcScope.level == level) {
                return funcScope;
            }
        }
        return CONTEXT;
    }

}
