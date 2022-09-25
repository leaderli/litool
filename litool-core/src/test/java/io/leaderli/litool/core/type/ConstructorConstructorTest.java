package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/25
 */
class ConstructorConstructorTest {

    @Test
    void get() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();

        Supplier<ArrayList<String>> arrayListSupplier = constructorConstructor.get(LiTypeToken.of(ArrayList.class));
        Assertions.assertNotNull(arrayListSupplier.get());
    }
}
