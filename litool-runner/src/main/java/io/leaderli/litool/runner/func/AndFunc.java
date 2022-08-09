package io.leaderli.litool.runner.func;

@FuncClass("and")
public class AndFunc {

    public String invoke(String... str) {
        return Boolean.FALSE.toString();
    }

}
