package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.type.LiTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/25
 */
class TypeAdaptersTest {


    @Test
    void test() {

        TypeAdapter<?> typeAdapter = TypeAdapters.PRIMITIVE_FACTORY.create(new Lean(), LiTypeToken.of(int.class));

        Assertions.assertEquals(1, typeAdapter.read("1"));
        Assertions.assertEquals(1, typeAdapter.read(1));
        Assertions.assertEquals(1, typeAdapter.read(1.0));
        Assertions.assertThrows(IllegalStateException.class, () -> typeAdapter.read(true));
        Assertions.assertThrows(IllegalStateException.class, () -> typeAdapter.read("a"));

    }
}
