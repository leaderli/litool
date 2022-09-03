package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitState;

/**
 * @author leaderli
 * @since 2022/8/31 11:05 AM
 */
public class LiraBit extends BitState {


    /**
     * lira has execute to the end
     */
    public static final int ITR = 1;
    public static final int LIMIT = 1 << 1;

    public LiraBit(int states) {
        super(states);
    }

    public static boolean isTerminal(int state) {
        return of(state).miss(ITR);
    }

    public static LiraBit of(int states) {
        return new LiraBit(states);
    }
}
