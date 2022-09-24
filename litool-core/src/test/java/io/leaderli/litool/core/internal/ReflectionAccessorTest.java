package io.leaderli.litool.core.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/25
 */
class ReflectionAccessorTest {

    @Test
    void getInstance() {

        ReflectionAccessor instance = ReflectionAccessor.getInstance();
        Assertions.assertDoesNotThrow(() -> instance.makeAccessible(instance.getClass().getMethods()[0]));
    }
}
