package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/24 3:23 PM
 */
@SuppressWarnings("rawtypes")
class LiTypeTokenTest {


    public Map map;

    @Test
    void of() throws NoSuchFieldException {

        Assertions.assertNotEquals(LiTypeToken.of(int.class), LiTypeToken.of(Integer.class));

        LiTypeToken<ArrayList<String>> token = new LiTypeToken<ArrayList<String>>() {
        };
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, ArrayList.class, String.class), token.getType());


        LiTypeToken<Map> tokenMap = LiTypeToken.ofType(this.getClass().getField("map").getGenericType());
        Assertions.assertEquals("[K, V]", Arrays.toString(tokenMap.getActualTypeArguments()));
        Assertions.assertEquals("[K, V]", Arrays.toString(LiTypeToken.of(Map.class).getActualTypeArguments()));

        Assertions.assertSame(TypeUtil.EMPTY_TYPE_ARRAY, LiTypeToken.of(Runnable.class).getActualTypeArguments());


    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    void testEquals() {

        Assertions.assertEquals(LiTypeToken.of(Bean.class).hashCode(), LiTypeToken.of(Bean.class).hashCode());
        Assertions.assertEquals(new LiTypeToken<Bean>() {
        }.hashCode(), new LiTypeToken<Bean>() {
        }.hashCode());

        Assertions.assertEquals(LiTypeToken.of(Bean.class), LiTypeToken.of(Bean.class));
        Assertions.assertEquals(new LiTypeToken<Bean>() {
        }, new LiTypeToken<Bean>() {
        });

    }

    private static class Bean {

    }

    @Test
    void test() {

        LiTypeToken<Function<String, Integer>> liTypeToken = new LiTypeToken<Function<String, Integer>>() {
        };
        Class<Function<String, Integer>> type = liTypeToken.getGenericType();
        Assertions.assertNotNull(type);


    }

}
