package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/9/21
 */
class ParameterizedTypeImplTest {


    @Test
    void make() {

        ParameterizedTypeImpl make = ParameterizedTypeImpl.make(Consumer.class);


        assertEquals("java.util.function.Consumer<T>", make.toString());
        make = ParameterizedTypeImpl.make(null, Consumer.class, String.class);
        assertEquals("java.util.function.Consumer<java.lang.String>", make.toString());

        make = ParameterizedTypeImpl.make(make);
        assertEquals("java.util.function.Consumer<java.lang.String>", make.toString());

        assertArrayEquals(new Type[]{String.class}, make.getActualTypeArguments());
        assertArrayEquals(new Class[]{String.class}, make.getActualClassArguments());
        assertEquals(String.class, make.getActualClassArgument().get());
        assertNull(make.getActualClassArgument(1).get());


    }

    @Test
    void getActualTypeArguments() {
        Assertions.assertArrayEquals(new Type[]{Object.class, Object.class}, ParameterizedTypeImpl.make(Map.class).getActualClassArguments());
        Assertions.assertSame(TypeUtil.EMPTY_TYPE_ARRAY, ParameterizedTypeImpl.make(Runnable.class).getActualTypeArguments());
        Assertions.assertSame(TypeUtil.EMPTY_TYPE_ARRAY, ParameterizedTypeImpl.make(null, List.class).getActualTypeArguments());

    }

}
