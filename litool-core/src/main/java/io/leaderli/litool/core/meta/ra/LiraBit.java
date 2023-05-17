package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;
import io.leaderli.litool.core.meta.Lira;

/**
 * @author leaderli
 * @since 2022/8/31 11:05 AM
 */
class LiraBit extends BitPermission {


    /**
     * the iterator request, evert request only get one element
     *
     * @see SubscriptionRa#request(int)
     */
    public static final int ITR = 1;
    /**
     * the request before terminal have a limit action, so the infinity generator could be stop
     *
     * @see Lira#range()
     * @see io.leaderli.litool.core.collection.Generator
     */
    public static final int LIMIT = 1 << 1;

    public LiraBit(int states) {
        super(states);
    }

    public static boolean isTerminal(int state) {
        return of(state).lacks(ITR);
    }

    public static LiraBit of(int states) {
        return new LiraBit(states);
    }
}
