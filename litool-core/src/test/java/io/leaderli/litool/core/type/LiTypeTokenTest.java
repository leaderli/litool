package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        LiTypeToken<List<String>> token = LiTypeToken.getParameterized(ArrayList.class, String.class);
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, ArrayList.class, String.class), token.getType());


        LiTypeToken<Map> tokenMap = LiTypeToken.of(this.getClass().getField("map").getGenericType());
        Assertions.assertEquals("[K, V]", Arrays.toString(tokenMap.getActualTypeArguments()));


    }


}
