package io.leaderli.litool.runner.func;

@Instruct("and")
public class AndInstruct {

    public static Boolean invoke(Boolean... ands) {

        for (Boolean and : ands) {
            if (!and) {
                return false;
            }
        }
        return true;
    }

}
