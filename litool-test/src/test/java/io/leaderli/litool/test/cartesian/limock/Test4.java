package io.leaderli.litool.test.cartesian.limock;

import io.leaderli.litool.test.cartesian.CartesianMock;
import io.leaderli.litool.test2.limock.Foo2;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author leaderli
 * @since 2023/7/3 3:23 PM
 */
public class Test4 {

    @Test
    public void test3() {
        CartesianMock.reset();
        Foo2 foo2 = new Foo2();
        Map<String, Object[]> map = foo2.map();
        System.out.println(map);

    }

}
