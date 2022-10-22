package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
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


    private Out.In<String> in;

    @Test
    void testToString() {
        ParameterizedType para = (ParameterizedType) ReflectUtil.getField(getClass(), "in").get().getGenericType();
        Assertions.assertEquals("io.leaderli.litool.core.type.ParameterizedTypeImplTest$Out$In<java.lang.String>",
                para.toString());
    }

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

        Assertions.assertEquals("io.leaderli.litool.core.type.ParameterizedTypeImplTest$Out$In<T>",
                ParameterizedTypeImpl.make(Out.In.class).toString());


        Assertions.assertArrayEquals(new Class[]{String.class, Object.class}, ParameterizedTypeImpl.make(null, Map.class, String.class).getActualClassArguments());
    }

    @Test
    void getActualTypeArguments() {
        Assertions.assertArrayEquals(new Type[]{Object.class, Object.class}, ParameterizedTypeImpl.make(Map.class).getActualClassArguments());
        assertEquals(0, ParameterizedTypeImpl.make(Runnable.class).getActualTypeArguments().length);
        Assertions.assertSame(Object.class, ParameterizedTypeImpl.make(null, List.class).getActualClassArguments()[0]);
    }

    private static class Out {
        private static class In<T> {

        }
    }

}
