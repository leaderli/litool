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

        Lean lean = null;
        Assertions.assertEquals(1, typeAdapter.read("1", lean));
        Assertions.assertEquals(1, typeAdapter.read(1, lean));
        Assertions.assertEquals(1, typeAdapter.read(1.0, lean));
        Assertions.assertThrows(IllegalStateException.class, () -> typeAdapter.read(true, lean));
        Assertions.assertThrows(IllegalStateException.class, () -> typeAdapter.read("a", lean));

    }
}
