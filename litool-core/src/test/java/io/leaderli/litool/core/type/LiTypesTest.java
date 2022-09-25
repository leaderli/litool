package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/9/25
 */
class LiTypesTest<T> {

    @SuppressWarnings("unchecked")
    public final T[] T = (T[]) new Object[0];

    @Test
    void test() throws NoSuchFieldException {

        Assertions.assertEquals(int.class, LiTypes.getArrayComponentType(int[].class));
        Assertions.assertEquals(Integer.class, LiTypes.getArrayComponentType(Integer[].class));
        Type type = this.getClass().getField("T").getGenericType();
        Assertions.assertEquals(getClass().getTypeParameters()[0], LiTypes.getArrayComponentType(type));

    }
}
