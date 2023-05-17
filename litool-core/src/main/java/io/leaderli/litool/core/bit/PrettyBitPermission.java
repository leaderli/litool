package io.leaderli.litool.core.bit;

import java.util.Objects;

/**
 * 一个重新了 {@link #toString()} 方法的实现类，可以方便的查看具体权限的名称
 *
 * @author leaderli
 * @since 2022/9/22 8:51 AM
 */
public class PrettyBitPermission extends BitPermission {

    private final Class<?> statusClass;

    private PrettyBitPermission(Class<?> statusClass) {
        Objects.requireNonNull(statusClass);
        this.statusClass = statusClass;
    }

    public static PrettyBitPermission of(Class<?> stateClass) {
        return new PrettyBitPermission(stateClass);
    }

    @Override
    public String toString() {
        return BitStr.of(statusClass).beauty(stateFlags);
    }

}
