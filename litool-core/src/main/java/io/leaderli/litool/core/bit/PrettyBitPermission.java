package io.leaderli.litool.core.bit;

/**
 * 一个重新了 {@link #toString()} 方法的实现类，可以方便的查看具体权限的名称
 *
 * @author leaderli
 * @since 2022/9/22 8:51 AM
 */
public class PrettyBitPermission extends BitPermission {


    private PrettyBitPermission(Class<?> statusClass, int stateFlags) {
        super(stateFlags);
        bitStr = BitStr.of(statusClass);
    }

    private PrettyBitPermission(Class<?> statusClass, int stateFlags, int size) {
        super(stateFlags, size);
        bitStr = BitStr.of(statusClass);
    }

    public static PrettyBitPermission of(Class<?> stateClass) {
        return new PrettyBitPermission(stateClass, 0);
    }

    public static PrettyBitPermission of(Class<?> stateClass, int stateFlags) {
        return new PrettyBitPermission(stateClass, stateFlags);
    }

    public static PrettyBitPermission of(Class<?> stateClass, int stateFlags, int size) {
        return new PrettyBitPermission(stateClass, stateFlags, size);
    }
}
