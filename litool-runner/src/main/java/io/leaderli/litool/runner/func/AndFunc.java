package io.leaderli.litool.runner.func;

@FuncClass("and")
public class AndFunc {

    public Boolean invoke(Boolean... ands) {

        for (Boolean and : ands) {
            if (!and) {
                return false;
            }
        }
        return true;
    }

}
