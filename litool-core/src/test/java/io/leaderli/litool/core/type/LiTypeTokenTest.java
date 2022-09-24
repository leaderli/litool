package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/9/24 3:23 PM
 */
class LiTypeTokenTest {

    @Test
    void test() {

        Assertions.assertNotEquals(LiTypeToken.get(int.class), LiTypeToken.get(Integer.class));

        LiTypeToken<List<String>> token = LiTypeToken.getParameterized(ArrayList.class, String.class);
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, ArrayList.class, String.class), token.getType());
        System.out.println(token.getType());

        System.out.println(token.getRawType());

    }

}
