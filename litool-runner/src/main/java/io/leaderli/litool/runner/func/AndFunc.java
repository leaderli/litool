package io.leaderli.litool.runner.func;

@InnerFunc("and")
public class AndFunc {

    public static Boolean invoke(Boolean... ands) {

        for (Boolean and : ands) {
            if (!and) {
                return false;
            }
        }
        return true;
    }

}
