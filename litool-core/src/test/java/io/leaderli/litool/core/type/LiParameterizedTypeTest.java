package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/9/21
 */
class LiParameterizedTypeTest {


    @Test
    void make() {

        LiParameterizedType make = LiParameterizedType.make(Consumer.class);
        Assertions.assertEquals("java.util.function.Consumer<T>", make.toString());
        make = LiParameterizedType.make(Consumer.class, null, String.class);
        Assertions.assertEquals("java.util.function.Consumer<java.lang.String>", make.toString());


    }

}
