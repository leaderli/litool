package io.leaderli.litool.test;

import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2023/6/30 9:59 AM
 */
public class LiTest2 {


    static void init() {

        LiMock.mock(MockMap.class);
        Map map = new MockMap();

        LiMock.whenArgs(() -> map.get("123"), args -> new Function[]{

                (ArgsFunction) ar -> {
                    if ("123".equalsIgnoreCase((String) ar[0])) {

                        return "456";
                    }
                    return ar[0];
                },
                (ArgsFunction) ar -> {
                    if ("123".equalsIgnoreCase((String) ar[0])) {

                        return null;
                    }
                    return ar[0];
                }
        });
    }

    @MockInit
    @LiTest
    void test() {
        Map map = new MockMap();
        System.out.println(map.get("123"));
        System.out.println(map.get("124"));
    }
}
