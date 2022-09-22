package io.leaderli.litool.core.bit;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/9/22 8:51 AM
 */
public class BitStateOf extends BitState {

    private final Class<?> stateClass;

    private BitStateOf(Class<?> stateClass) {
        Objects.requireNonNull(stateClass);
        this.stateClass = stateClass;
    }

    public static BitStateOf the(Class<?> stateClass) {
        return new BitStateOf(stateClass);
    }

    @Override
    public String toString() {
        return BitStr.of(stateClass).beauty(states);
    }

}
