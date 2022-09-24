package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/9/21
 */
class LiParameterizedTypeTest {


    @Test
    void test() {


    }

    @Test
    void make() {

        LiParameterizedType make = LiParameterizedType.make(Consumer.class);


        assertEquals("java.util.function.Consumer<T>", make.toString());
        make = LiParameterizedType.make(Consumer.class, null, String.class);
        assertEquals("java.util.function.Consumer<java.lang.String>", make.toString());

        make = LiParameterizedType.make(make);
        assertEquals("java.util.function.Consumer<java.lang.String>", make.toString());

        assertArrayEquals(new Type[]{String.class}, make.getActualTypeArguments());
        assertArrayEquals(new Class[]{String.class}, make.getActualClassArguments());
        assertEquals(String.class, make.getActualClassArgument().get());
        assertNull(make.getActualClassArgument(1).get());

    }

}
