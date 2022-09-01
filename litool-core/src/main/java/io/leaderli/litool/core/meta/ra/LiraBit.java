package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitState;

/**
 * @author leaderli
 * @since 2022/8/31 11:05 AM
 */
public class LiraBit extends BitState {

    /**
     * request a element from {@link  ArraySome}
     */
    public static final int REQUEST = 1;
    /**
     * a signal have reached the  {@link  Subscriber}
     */
    public static final int ARRIVED = 1 << 1;

    /**
     * lira has execute to the end
     */
    public static final int COMPLETE = 1 << 2;

}
