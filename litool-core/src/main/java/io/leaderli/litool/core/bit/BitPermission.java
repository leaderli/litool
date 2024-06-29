package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.exception.LiAssertUtil;

/**
 * 使用非负二进制表示权限，并使用位运算操作权限。
 *
 * @see PrettyBitPermission
 */
public abstract class BitPermission {
    protected BitStr bitStr = BitStr.of(getClass());

    /**
     * 一个非负整数，用于保存权限标记，使用二进制位表示权限。1表示有权限，0表示无权限。
     */
    protected int stateFlags;

    protected BitPermission() {
    }

    protected BitPermission(int stateFlags) {
        this.stateFlags = stateFlags;
    }

    /**
     * 设置权限。
     *
     * @param newStateFlags 要设置的新状态。
     */
    public void setState(int newStateFlags) {
        assertNonNegative(newStateFlags);
        this.stateFlags = newStateFlags;
    }

    /**
     * 检查指定整数是否为非负数。
     *
     * @param value 要检查的整数。
     * @throws IllegalArgumentException 如果指定整数为负数。
     */
    private void assertNonNegative(int value) {
        LiAssertUtil.assertTrue(value > -1, new IllegalArgumentException(value + ""));
    }

    /**
     * 添加一个或多个权限。
     *
     * @param flags 要启用的权限。
     */
    public void enable(int flags) {
        assertNonNegative(flags);
        this.stateFlags |= flags;
    }

    /**
     * 移除一个或多个权限。
     *
     * @param flags 要禁用的权限。
     */
    public void disable(int flags) {
        assertNonNegative(flags);
        this.stateFlags &= ~flags;
    }

    /**
     * 检查指定的权限是否存在。
     *
     * @param flags 要检查的权限。
     * @return 如果所有指定的权限都存在，则返回true，否则返回false。
     */
    public boolean has(int flags) {
        if (flags < 0) {
            return false;
        }
        return (this.stateFlags & flags) == flags;
    }

    /**
     * 检查指定的权限是否不存在。
     *
     * @param flags 要检查的权限。
     * @return 如果所有指定的权限都不存在，则返回true，否则返回false。
     */
    public boolean lacks(int flags) {
        if (flags < 0) {
            return true;
        }
        return (this.stateFlags & flags) == 0;
    }

    /**
     * 检查指定的权限是否是唯一存在的权限。
     *
     * @param flags 要检查的权限。
     * @return 如果指定的权限是唯一存在的权限，则返回true，否则返回false。
     */
    public boolean only(int flags) {
        return this.stateFlags == flags;
    }

    /**
     * 检查是否不存在任何权限。
     *
     * @return 如果不存在任何权限，则返回true，否则返回false。
     */
    public boolean none() {
        return stateFlags == 0;
    }

    /**
     * 检查是否存在任何权限。
     *
     * @return 如果存在任何权限，则返回true，否则返回false。
     */
    public boolean any() {
        return stateFlags > 0;
    }

    /**
     * @return 根据类的枚举值，返回其字符串表现形式
     * @see BitStr#beauty(int)
     */
    @Override
    public String toString() {
        return bitStr.beauty(stateFlags);
    }

    /**
     * @param state 二进制状态位
     * @return 根据类的枚举值，返回其字符串表现形式
     * @see BitStr#beauty(int)
     */

    public String toString(int state) {
        return bitStr.beauty(state);
    }

    /**
     * 返回此BitState对象的当前状态。
     *
     * @return 此BitState对象的当前状态，作为一个整数。
     */
    public int getCurrentState() {
        return stateFlags;
    }
}
