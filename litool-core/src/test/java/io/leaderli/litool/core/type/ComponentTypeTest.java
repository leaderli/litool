package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class ComponentTypeTest {


@Test
void componentType() {
    ComponentType<String> instance = new ComponentType<String>() {
    };

    Assertions.assertSame(String.class, instance.componentType());
    ComponentType<Integer> instance2 = new ComponentType<Integer>() {
    };
    Assertions.assertSame(Integer.class, instance2.componentType());

}
}
