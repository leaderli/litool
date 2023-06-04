package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        LiTypeToken<List<String>> token = LiTypeToken.ofParameterized(ArrayList.class, String.class);
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, ArrayList.class, String.class), token.getType());


        LiTypeToken<Map> tokenMap = LiTypeToken.ofType(this.getClass().getField("map").getGenericType());
        Assertions.assertEquals("[K, V]", Arrays.toString(tokenMap.getActualTypeArguments()));
        Assertions.assertEquals("[K, V]", Arrays.toString(LiTypeToken.of(Map.class).getActualTypeArguments()));

        Assertions.assertSame(TypeUtil.EMPTY_TYPE_ARRAY, LiTypeToken.of(Runnable.class).getActualTypeArguments());


    }

    @Test
    void testEquals() {

        Assertions.assertEquals(LiTypeToken.ofParameterized(Bean.class).hashCode(),
                LiTypeToken.ofParameterized(Bean.class).hashCode());

        Assertions.assertEquals(LiTypeToken.ofParameterized(Bean.class), LiTypeToken.ofParameterized(Bean.class));

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
