package io.leaderli.litool.test.limock;

/**
 * @author leaderli
 * @since 2022/10/27 9:05 AM
 */
public class StaticBlock {
    public static int size = 1;

    static {
        StaticBlock.size = 100;
        //noinspection ConstantConditions
        if (size > 1) {
            throw new RuntimeException();
        }
    }


    public static int size() {
        return StaticBlock.size;
    }


}
