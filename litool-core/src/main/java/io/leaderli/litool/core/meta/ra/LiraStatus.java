package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;

/**
 * @author leaderli
 * @since 2022/8/31 11:05 AM
 */
class LiraStatus extends BitPermission {

    /**
     * request a element from {@link  ArraySome}
     */
    public static final int REQUEST = 0b1;
    /**
     * a signal have reached the  {@link  Subscriber}
     */
    public static final int ARRIVED = 0b10;

    /**
     * lira has execute to the end
     */
    public static final int COMPLETE = 0b1000;
}
