package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.exception.LiAssertUtil;

/**
 * Use non-negative binary to express permissions, use bitwise operations to modify permissions
 *
 * @author leaderli
 * @since 2022/8/16
 */
public abstract class BitState {

    protected BitState() {

    }

    protected BitState(int states) {
        this.states = states;
    }

    /**
     * A non-negative int to hold the permission token, save permission with binary position. 1 means permission, 0
     * means
     * no permission
     */
    private int states;


    /**
     * Set the permissions
     *
     * @param permissions {@link #states}
     */
    public void set(int permissions) {
        non_negative(permissions);
        this.states = permissions;
    }

    private void non_negative(int permissions) {
        LiAssertUtil.assertTrue(permissions > -1);
    }

    /**
     * Add one or more permission
     *
     * @param permissions {@link #states}
     */
    public void enable(int permissions) {
        non_negative(permissions);
        this.states |= permissions;
    }

    /**
     * Remove one or more permission
     *
     * @param permissions {@link #states}
     */
    public void disable(int permissions) {
        non_negative(permissions);
        this.states &= ~permissions;
    }

    /**
     * Have the permissions
     *
     * @param permissions {@link #states}
     * @return have the permissions
     */
    public boolean have(int permissions) {
        if (permissions < 0) {
            return false;
        }
        return (this.states & permissions) == permissions;
    }

    /**
     * Don't have the permissions
     *
     * @param permissions {@link #states}
     * @return don't have the permissions
     */
    public boolean miss(int permissions) {
        if (permissions < 0) {
            return true;
        }
        return (this.states & permissions) == 0;
    }

    /**
     * only have the permissions
     *
     * @param permissions {@link #states}
     * @return {@code this.permissions == permissions}
     */
    public boolean only(int permissions) {
        return this.states == permissions;
    }


    /**
     * Don't have any permission
     *
     * @return {@code permissions == 0 }
     */
    public boolean none() {
        return states == 0;
    }

    /**
     * @return {@code permissions > 0 }
     */
    public boolean any() {
        return states > 0;
    }

    @Override
    public String toString() {
        return BitStr.of(this.getClass()).beauty(states);
    }

    public int get() {
        return states;
    }
}

